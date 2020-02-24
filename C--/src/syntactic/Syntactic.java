package syntactic;

import categories.CategoryList;
import lexic.Lexical;
import token.Token;

import java.util.ArrayList;
import java.util.List;

public class Syntactic {

    private Lexical lexicalAnalyzer;
    private Token token;

    public Syntactic(String args) {
        this.lexicalAnalyzer = new Lexical(args);
    }

    private void Error(String message) {
        System.err.println("Error: "+ message + " In line " + (token.getLineNumber() - 1) + " and column " + token.getColumnNumber());
        System.exit(0);
    }

    private boolean checkToken(ArrayList<CategoryList> categories) {

        for(CategoryList cat : categories) {
            if(token.getTokenCategory() == cat) {
                return true;
            }
        }

        return false;
    }

//TODO: SABER COMO LIDAR COM O EPSILON E O EOF
    public void S() {
//        S = VarType Decl S
//          | 'void' DeclFun S
//          | EPSILON

        token = lexicalAnalyzer.nextToken();
        // int 2;
        if (checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            VarType();
            Decl();
            S();
        }

        if(checkToken(new ArrayList<>(List.of(CategoryList.Tvoid)))) {
            token = lexicalAnalyzer.nextToken();
            DeclFun();
            S();
        }

//        while(true) {
//            token = lexicalAnalyzer.nextToken();
//            if (token != null) {
//                System.out.println(token.toString());
//                if (token.getTokenCategory() == CategoryList.TEOF) {
//                    return;
//                }
//            }
//        }

    }

    private void Decl() {
        //Decl        = '[' DeclR
        //            | DeclAux
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegSqBrac)))) {
            token = lexicalAnalyzer.nextToken();
            DeclR();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId, CategoryList.Tmain, CategoryList.TnameId)))) {
            DeclAux();
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TbegSqBrac', 'TfuncId', 'Tmain', 'TnameId'.");
        }

    }

    private void DeclR() {
        //DeclR       = ']' DeclFun
        //            | ArrSize ']' DeclAux
        if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
            token = lexicalAnalyzer.nextToken();
            DeclFun();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId, CategoryList.TcteInt)))){
            ArrSize();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
                token = lexicalAnalyzer.nextToken();
                DeclAux();
            }  else {
                Error("Unexpected token " + token.getId() + ". Expected 'TendSqBrac'.");
            }
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TendSqBrac', 'TnameId', 'TcteInt'.");
        }
    }

    private void DeclAux() {
        //DeclAux     = DeclFun
        //            | DeclVar
        if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId, CategoryList.Tmain)))) {
            DeclFun();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            DeclVar();
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TfuncId'. 'Tmain', 'TnameId'.");
        }

    }

    private void DeclFun() {
        //DeclFun     = IdFun '(' Param ')' OpSentCl
        if (checkToken(new ArrayList<>(List.of(CategoryList.TfuncId, CategoryList.Tmain)))) {
            IdFun();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                Param();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                    token = lexicalAnalyzer.nextToken();
                    OpSentCl();
                } else {
                    Error("Unexpected token " + token.getId() + ". Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TbegBrac'.");
            }
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TfuncId', 'Tmain'.");
        }
    }

    private void DeclVar() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            token = lexicalAnalyzer.nextToken();
            Atr();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TsemiCol'.");
            }
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TnameId'.");
        }
    }

    private void IdFun() {
        token = lexicalAnalyzer.nextToken();
    }

    private void ArrSize() {
        token = lexicalAnalyzer.nextToken();
    }

    private void VarType() {
        token = lexicalAnalyzer.nextToken();
    }

    private void Param() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            ParamList();
        }
    }

    private void Atr() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopEq)))) {
            token = lexicalAnalyzer.nextToken();
            Ec();
        }
    }

    private void OpSentCl() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegCrBrac)))) {
            token = lexicalAnalyzer.nextToken();
            Sent();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendCrBrac)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TendCrBrac'.");
            }
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TbegCrBrac'.");
        }
    }

    private void ParamList() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            ParVar();
            ParamListR();
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'Tint', 'Tfloat', 'Tstring', 'Tbool', 'Tchar'.");
        }
    }

    private void Sent() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            VarType();
            OptArray();
            DeclVar();
            Sent();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId)))) {
            FunCall();
            Sent();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tprint, CategoryList.Tget, CategoryList.Tif, CategoryList.Tfrom, CategoryList.Tduring)))) {
            Commands();
            Sent();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Treturn)))) {
            token = lexicalAnalyzer.nextToken();
            ReturnParam();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TsemiCol'.");
            }
        }
    }

    private void ParVar() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            VarType();
            OptFnArray();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TnameId'.");
            }
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'Tint', 'Tfloat', 'Tstring', 'Tbool', 'Tchar'.");
        }
    }

    private void ParamListR() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tcomma)))) {
            token = lexicalAnalyzer.nextToken();
            ParamList();
        }
    }

    private void OptArray() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegSqBrac)))) {
            token = lexicalAnalyzer.nextToken();
            ArrSize();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TendSqBrac'.");
            }
        }
    }

    private void FunCall() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId)))) {
            token = lexicalAnalyzer.nextToken();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                ParCall();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                    token = lexicalAnalyzer.nextToken();
                    if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                        token = lexicalAnalyzer.nextToken();
                    } else {
                        Error("Unexpected token " + token.getId() + ". Expected 'TsemiCol'.");
                    }
                } else {
                    Error("Unexpected token " + token.getId() + ". Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TbegBrac'.");
            }
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TfuncId'.");
        }
    }

    private void Commands() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tprint)))) {
            token = lexicalAnalyzer.nextToken();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TcteString)))) {
                    token = lexicalAnalyzer.nextToken();
                    PrintOpt();
                    if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                        token = lexicalAnalyzer.nextToken();
                        if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                            token = lexicalAnalyzer.nextToken();
                        } else {
                            Error("Unexpected token " + token.getId() + ". Expected 'TsemiCol'.");
                        }
                    } else {
                        Error("Unexpected token " + token.getId() + ". Expected 'TendBrac'.");
                    }
                } else {
                    Error("Unexpected token " + token.getId() + ". Expected 'TcteString'.");
                }
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TbegBrac'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tget)))) {
            token = lexicalAnalyzer.nextToken();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                GetVarList();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                    token = lexicalAnalyzer.nextToken();
                    if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                        token = lexicalAnalyzer.nextToken();
                    } else {
                        Error("Unexpected token " + token.getId() + ". Expected 'TsemiCol'.");
                    }
                } else {
                    Error("Unexpected token " + token.getId() + ". Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TbegBrac'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tif)))) {
            token = lexicalAnalyzer.nextToken();
            Eb();
            OpSentCl();
            ElseComm();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tfrom)))) {
            token = lexicalAnalyzer.nextToken();
            Var();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TopEq)))) {
                token = lexicalAnalyzer.nextToken();
                Ea();
                if(checkToken(new ArrayList<>(List.of(CategoryList.Tto)))) {
                    token = lexicalAnalyzer.nextToken();
                    Ea();
                    if(checkToken(new ArrayList<>(List.of(CategoryList.Tincrement)))) {
                        token = lexicalAnalyzer.nextToken();
                        Ea();
                        OpSentCl();
                    } else {
                        Error("Unexpected token " + token.getId() + ". Expected 'Tincrement'.");
                    }
                } else {
                    Error("Unexpected token " + token.getId() + ". Expected 'Tto'.");
                }
            } else {
                Error("Unexpected token " + token.getId() + ". Expected 'TopEq'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tduring)))) {
            token = lexicalAnalyzer.nextToken();
            Eb();
            OpSentCl();
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'Tprint', 'Tget', 'Tif', 'Tfrom', 'Tduring'.");
        }
    }

    private void ReturnParam() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            Ec();
        }
    }

    private void OptFnArray() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegSqBrac)))) {
            token = lexicalAnalyzer.nextToken();
            OptFnArrayR();
        }
    }

    private void OptFnArrayR() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId, CategoryList.TcteInt)))) {
            ArrSize();
        }
        if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
            token = lexicalAnalyzer.nextToken();
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TendSqBrac'.");
        }
    }

    private void ParCall() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            ParCallList();
        }
    }

    private void ParCallList() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            Ec();
            ParCallListR();
        } else {
            Error("Unexpected token " + token.getId() + ". Expected 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }
    
}
