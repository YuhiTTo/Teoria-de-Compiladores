#include <fstream>
#include <iostream>


#include "FinZenDriver.h"
#include "FinZenLexer.h"
#include "FinZenParser.h"


using namespace antlr4;
using namespace std;

int main(int argc, const char *argv[]) {
  ifstream ifile;
  if (argc > 1) {
    ifile.open(argv[1]);
    if (!ifile.is_open()) {
      cerr << "Error: Could not open file " << argv[1] << endl;
      exit(-1);
    }
  }
  istream &stream = argc > 1 ? ifile : cin;
  ANTLRInputStream input(stream);
  FinZenLexer lexer(&input);
  CommonTokenStream tokens(&lexer);
  FinZenParser parser(&tokens);

  tree::ParseTree *tree = parser.programa();

  if (parser.getNumberOfSyntaxErrors() > 0) {
    cerr << "Syntax errors found. Aborting." << endl;
    return -1;
  }

  FinZenDriver *driver = new FinZenDriver();
  driver->visit(tree);

  delete driver;

  return 0;
}
