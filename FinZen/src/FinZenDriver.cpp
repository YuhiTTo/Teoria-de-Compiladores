#include "FinZenDriver.h"
#include <iostream>
#include <vector>

#include "llvm/Transforms/InstCombine/InstCombine.h"
#include "llvm/Transforms/Scalar/GVN.h"
#include "llvm/Transforms/Scalar/Reassociate.h"
#include "llvm/Transforms/Scalar/SimplifyCFG.h"
#include "llvm/Transforms/Utils/Mem2Reg.h"

using namespace llvm;
using namespace std;

FinZenDriver::FinZenDriver()
    : context(), module(make_unique<Module>("FinZen", context)),
      irBuilder(make_unique<IRBuilder<>>(context)) {
  initializeOptimizations();
}

void FinZenDriver::initializeOptimizations() {

  LAM = make_unique<LoopAnalysisManager>();
  FAM = make_unique<FunctionAnalysisManager>();
  CGAM = make_unique<CGSCCAnalysisManager>();
  MAM = make_unique<ModuleAnalysisManager>();
  PB = make_unique<PassBuilder>();

  PB->registerModuleAnalyses(*MAM);
  PB->registerCGSCCAnalyses(*CGAM);
  PB->registerFunctionAnalyses(*FAM);
  PB->registerLoopAnalyses(*LAM);
  PB->crossRegisterProxies(*LAM, *FAM, *CGAM, *MAM);

  FPM = make_unique<FunctionPassManager>();

  FPM->addPass(PromotePass());
  FPM->addPass(InstCombinePass());
  FPM->addPass(ReassociatePass());
  FPM->addPass(GVNPass());
  FPM->addPass(SimplifyCFGPass());
}

string FinZenDriver::unquote(const string &s) {
  if (s.size() >= 2 && s.front() == '"' && s.back() == '"') {
    return s.substr(1, s.size() - 2);
  }
  return s;
}

Value *FinZenDriver::createPrintfCall(const string &fmt, vector<Value *> args) {

  Value *formatStr = irBuilder->CreateGlobalString(fmt, "fmt");

  vector<Value *> printfArgs;
  printfArgs.push_back(formatStr);
  printfArgs.insert(printfArgs.end(), args.begin(), args.end());

  return irBuilder->CreateCall(printfFunc, printfArgs);
}

std::any FinZenDriver::visitPrograma(FinZenParser::ProgramaContext *ctx) {

  FunctionType *mainType = FunctionType::get(Type::getInt32Ty(context), false);
  Function *mainFunc =
      Function::Create(mainType, Function::ExternalLinkage, "main", *module);

  BasicBlock *entry = BasicBlock::Create(context, "entry", mainFunc);
  irBuilder->SetInsertPoint(entry);

  vector<Type *> printfArgsTypes;
  printfArgsTypes.push_back(PointerType::getUnqual(context));
  FunctionType *printfType =
      FunctionType::get(Type::getInt32Ty(context), printfArgsTypes, true);
  printfFunc = module->getOrInsertFunction("printf", printfType);

  vector<Type *> addArgs;
  addArgs.push_back(PointerType::getUnqual(context));
  addArgs.push_back(Type::getDoubleTy(context));
  addArgs.push_back(PointerType::getUnqual(context));
  FunctionType *addType =
      FunctionType::get(Type::getVoidTy(context), addArgs, false);
  runtimeAgregarGasto =
      module->getOrInsertFunction("runtime_agregar_gasto", addType);

  FunctionType *listType = FunctionType::get(Type::getVoidTy(context), false);
  runtimeListarGastos =
      module->getOrInsertFunction("runtime_listar_gastos", listType);

  vector<Type *> filterArgs;
  filterArgs.push_back(PointerType::getUnqual(context));
  FunctionType *filterType =
      FunctionType::get(Type::getVoidTy(context), filterArgs, false);
  runtimeFiltrarGastos =
      module->getOrInsertFunction("runtime_filtrar_gastos", filterType);

  module->getOrInsertGlobal("TotalIngresos", Type::getDoubleTy(context));
  GlobalVariable *gIngresos = module->getNamedGlobal("TotalIngresos");
  gIngresos->setLinkage(GlobalValue::InternalLinkage);
  gIngresos->setInitializer(ConstantFP::get(Type::getDoubleTy(context), 0.0));

  module->getOrInsertGlobal("TotalGastos", Type::getDoubleTy(context));
  GlobalVariable *gGastos = module->getNamedGlobal("TotalGastos");
  gGastos->setLinkage(GlobalValue::InternalLinkage);
  gGastos->setInitializer(ConstantFP::get(Type::getDoubleTy(context), 0.0));

  visitChildren(ctx);

  irBuilder->CreateRet(ConstantInt::get(Type::getInt32Ty(context), 0));

  verifyModule(*module, &errs());

  FPM->run(*mainFunc, *FAM);

  module->print(outs(), nullptr);

  return std::any();
}

std::any FinZenDriver::visitPresupuesto_stmt(
    FinZenParser::Presupuesto_stmtContext *ctx) {
  string cat = unquote(ctx->CADENA()->getText());
  double amount = stod(ctx->NUMERO()->getText());

  string varName = "Presupuesto_" + cat;
  module->getOrInsertGlobal(varName, Type::getDoubleTy(context));
  GlobalVariable *gVar = module->getNamedGlobal(varName);
  gVar->setLinkage(GlobalValue::InternalLinkage);
  gVar->setInitializer(ConstantFP::get(Type::getDoubleTy(context), amount));

  presupuestos[cat] = gVar;

  return std::any();
}

std::any FinZenDriver::visitMovimiento(FinZenParser::MovimientoContext *ctx) {

  string tipo = ctx->children[0]->getText();
  string nombre = unquote(ctx->children[1]->getText());

  double monto = 0.0;
  string categoria = "";

  for (size_t i = 2; i < ctx->children.size(); ++i) {
    string t = ctx->children[i]->getText();
    if ((t == "monto" || t == "actual" || t == "categoria") &&
        i + 2 < ctx->children.size()) {
      string val = ctx->children[i + 2]->getText();
      if (t == "categoria") {
        categoria = unquote(val);
      } else {
        monto = stod(val);
      }
      i += 2;
    }
  }

  if (tipo == "ingreso") {
    GlobalVariable *gIngresos = module->getNamedGlobal("TotalIngresos");
    Value *val = irBuilder->CreateLoad(Type::getDoubleTy(context), gIngresos);
    Value *add = irBuilder->CreateFAdd(
        val, ConstantFP::get(Type::getDoubleTy(context), monto));
    irBuilder->CreateStore(add, gIngresos);

  } else if (tipo == "gasto") {
    GlobalVariable *gGastos = module->getNamedGlobal("TotalGastos");
    Value *val = irBuilder->CreateLoad(Type::getDoubleTy(context), gGastos);
    Value *add = irBuilder->CreateFAdd(
        val, ConstantFP::get(Type::getDoubleTy(context), monto));
    irBuilder->CreateStore(add, gGastos);

    string catVarName = "Gasto_" + categoria;
    GlobalVariable *gCat = module->getNamedGlobal(catVarName);
    if (!gCat) {
      module->getOrInsertGlobal(catVarName, Type::getDoubleTy(context));
      gCat = module->getNamedGlobal(catVarName);
      gCat->setLinkage(GlobalValue::InternalLinkage);
      gCat->setInitializer(ConstantFP::get(Type::getDoubleTy(context), 0.0));
    }
    Value *catVal = irBuilder->CreateLoad(Type::getDoubleTy(context), gCat);
    Value *catAdd = irBuilder->CreateFAdd(
        catVal, ConstantFP::get(Type::getDoubleTy(context), monto));
    irBuilder->CreateStore(catAdd, gCat);

    Value *nameStr = irBuilder->CreateGlobalString(nombre);
    Value *catStr = irBuilder->CreateGlobalString(categoria);
    Value *montoVal = ConstantFP::get(Type::getDoubleTy(context), monto);
    irBuilder->CreateCall(runtimeAgregarGasto, {nameStr, montoVal, catStr});
  }

  return std::any();
}

std::any FinZenDriver::visitComando(FinZenParser::ComandoContext *ctx) {
  string cmd = ctx->children[0]->getText();

  if (cmd == "total_ingresos") {
    GlobalVariable *gIngresos = module->getNamedGlobal("TotalIngresos");
    Value *val = irBuilder->CreateLoad(Type::getDoubleTy(context), gIngresos);
    createPrintfCall("Total Ingresos: %.2f\n", {val});

  } else if (cmd == "total_gastos") {
    GlobalVariable *gGastos = module->getNamedGlobal("TotalGastos");
    Value *val = irBuilder->CreateLoad(Type::getDoubleTy(context), gGastos);
    createPrintfCall("Total Gastos: %.2f\n", {val});

  } else if (cmd == "diferencia") {
    GlobalVariable *gIngresos = module->getNamedGlobal("TotalIngresos");
    GlobalVariable *gGastos = module->getNamedGlobal("TotalGastos");
    Value *vIng = irBuilder->CreateLoad(Type::getDoubleTy(context), gIngresos);
    Value *vGas = irBuilder->CreateLoad(Type::getDoubleTy(context), gGastos);
    Value *diff = irBuilder->CreateFSub(vIng, vGas);
    createPrintfCall("Diferencia: %.2f\n", {diff});

  } else if (cmd == "resumen_categoria") {
    string cat = unquote(ctx->children[1]->getText());
    string catVarName = "Gasto_" + cat;

    GlobalVariable *gCat = module->getNamedGlobal(catVarName);
    if (gCat) {
      Value *val = irBuilder->CreateLoad(Type::getDoubleTy(context), gCat);
      createPrintfCall("Resumen " + cat + ": %.2f\n", {val});
    } else {
      createPrintfCall("Resumen " + cat + ": 0.00\n", {});
    }

  } else if (cmd == "listar_gastos") {
    irBuilder->CreateCall(runtimeListarGastos, {});

  } else if (cmd == "filtrar_gastos") {
    string cat = unquote(ctx->children[1]->getText());
    Value *catStr = irBuilder->CreateGlobalString(cat);
    irBuilder->CreateCall(runtimeFiltrarGastos, {catStr});

  } else if (cmd == "diferencia_presupuestos") {
    createPrintfCall("--- Diferencia Presupuestos ---\n", {});
    for (auto const &[cat, gPresupuesto] : presupuestos) {
      Value *vPres =
          irBuilder->CreateLoad(Type::getDoubleTy(context), gPresupuesto);

      string catVarName = "Gasto_" + cat;
      GlobalVariable *gGasto = module->getNamedGlobal(catVarName);
      Value *vGasto;

      if (gGasto) {
        vGasto = irBuilder->CreateLoad(Type::getDoubleTy(context), gGasto);
      } else {
        vGasto = ConstantFP::get(Type::getDoubleTy(context), 0.0);
      }

      Value *diff = irBuilder->CreateFSub(vPres, vGasto);

      string fmt = " " + cat + ": Pres=%.2f, Actual=%.2f, Ahorro=%.2f\n";
      createPrintfCall(fmt, {vPres, vGasto, diff});

      Function *TheFunction = irBuilder->GetInsertBlock()->getParent();

      BasicBlock *ThenBB = BasicBlock::Create(context, "then", TheFunction);
      BasicBlock *MergeBB = BasicBlock::Create(context, "ifcont", TheFunction);

      Value *CondV = irBuilder->CreateFCmpOGT(vGasto, vPres, "alertcond");
      irBuilder->CreateCondBr(CondV, ThenBB, MergeBB);

      irBuilder->SetInsertPoint(ThenBB);
      string alertFmt = "  [!] ALERTA: Presupuesto excedido en " + cat + "!\n";
      createPrintfCall(alertFmt, {});
      irBuilder->CreateBr(MergeBB);

      irBuilder->SetInsertPoint(MergeBB);
    }
  }

  return std::any();
}
