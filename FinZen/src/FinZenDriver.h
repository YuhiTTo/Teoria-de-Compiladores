#pragma once

#include <any>
#include <map>
#include <memory>
#include <string>
#include <vector>

#include "llvm/IR/Function.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/LLVMContext.h"
#include "llvm/IR/Module.h"
#include "llvm/IR/PassManager.h"
#include "llvm/IR/Type.h"
#include "llvm/IR/Verifier.h"
#include "llvm/Passes/PassBuilder.h"
#include "llvm/Passes/StandardInstrumentations.h"

#include "FinZenBaseVisitor.h"

using namespace antlr4;
using namespace llvm;
using namespace std;

class FinZenDriver : public FinZenBaseVisitor {
private:
  LLVMContext context;
  unique_ptr<Module> module;
  unique_ptr<IRBuilder<>> irBuilder;

  unique_ptr<FunctionPassManager> FPM;
  unique_ptr<LoopAnalysisManager> LAM;
  unique_ptr<FunctionAnalysisManager> FAM;
  unique_ptr<CGSCCAnalysisManager> CGAM;
  unique_ptr<ModuleAnalysisManager> MAM;
  unique_ptr<PassBuilder> PB;

  map<string, GlobalVariable *> presupuestos;

  FunctionCallee printfFunc;
  FunctionCallee runtimeAgregarGasto;
  FunctionCallee runtimeListarGastos;
  FunctionCallee runtimeFiltrarGastos;
  Value *createPrintfCall(const string &fmt, vector<Value *> args);
  string unquote(const string &s);
  void initializeOptimizations();

public:
  FinZenDriver();

  virtual std::any visitPrograma(FinZenParser::ProgramaContext *ctx) override;
  virtual std::any
  visitPresupuesto_stmt(FinZenParser::Presupuesto_stmtContext *ctx) override;
  virtual std::any
  visitMovimiento(FinZenParser::MovimientoContext *ctx) override;
  virtual std::any visitComando(FinZenParser::ComandoContext *ctx) override;
};
