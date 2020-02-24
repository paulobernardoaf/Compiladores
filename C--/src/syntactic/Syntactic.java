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
        System.err.println("Error: "+ message + " In line " + token.getLineNumber() + " and column " + token.getColumnNumber());
        System.exit(0);
    }

    public void production(String left, String right) {
        String format = "%10s%s = %s";
        System.out.println(String.format(format, "", left, right));
    }

    private boolean checkToken(ArrayList<CategoryList> categories) {

        for(CategoryList cat : categories) {
            if(token.getTokenCategory() == cat) {
                return true;
            }
        }

        return false;
    }

    public void start() {
        token = lexicalAnalyzer.nextToken();
        S();

    }

//TODO: SABER COMO LIDAR COM O EPSILON E O EOF
    public void S() {
//        S = VarType Decl S
//          | 'void' DeclFun S
//          | EPSILON

        // int 2;
        if (checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            production("S", "VarType Decl S");
            VarType();
            Decl();
            S();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tvoid)))) {
            production("S", "'void' DeclFun S");
            token = lexicalAnalyzer.nextToken();
            DeclFun();
            S();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TEOF)))) {
            production("S", "EOF");
        } else {
            production("S", "EPSILON");
        }

//        while(true) {
//            token = lexicalAnalyzer.nextToken();
//            if (token != null) {
//                System.out.println(token.toString());
//                if (token.getTokenCategory() == CategoryList.TEOF) {
//                    return;
//                }
//            } else {
//                Error("TA NULO");
//            }
//        }

    }

    private void Decl() {
        //Decl        = '[' DeclR
        //            | DeclAux
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegSqBrac)))) {
            production("Decl", "'[' DeclR");
            token = lexicalAnalyzer.nextToken();
            DeclR();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId, CategoryList.Tmain, CategoryList.TnameId)))) {
            production("Decl", "DeclAux");
            DeclAux();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegSqBrac', 'TfuncId', 'Tmain', 'TnameId'.");
        }

    }

    private void DeclR() {
        //DeclR       = ']' DeclFun
        //            | ArrSize ']' DeclAux
        if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
            production("DeclR", "']' DeclFun");
            token = lexicalAnalyzer.nextToken();
            DeclFun();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId, CategoryList.TcteInt)))){
            production("DeclR", "ArrSize ']' DeclAux");
            ArrSize();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
                token = lexicalAnalyzer.nextToken();
                DeclAux();
            }  else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac', 'TnameId', 'TcteInt'.");
        }
    }

    private void DeclAux() {
        //DeclAux     = DeclFun
        //            | DeclVar
        if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId, CategoryList.Tmain)))) {
            production("DeclAux", "DeclFun");
            DeclFun();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("DeclAux", "DeclVar");
            DeclVar();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TfuncId'. 'Tmain', 'TnameId'.");
        }

    }

    private void DeclFun() {
        //DeclFun     = IdFun '(' Param ')' OpSentCl
        if (checkToken(new ArrayList<>(List.of(CategoryList.TfuncId, CategoryList.Tmain)))) {
            production("DeclFun", "IdFun '(' Param ')' OpSentCl");
            IdFun();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                Param();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                    token = lexicalAnalyzer.nextToken();
                    OpSentCl();
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TfuncId', 'Tmain'.");
        }
    }

    private void DeclVar() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("DeclVar", "'idVar' Atr ';'");
            token = lexicalAnalyzer.nextToken();
            Atr();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
        }
    }

    private void Atr() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopAtr)))) {
            production("Atr", "'=' Ec");
            token = lexicalAnalyzer.nextToken();
            Ec();
        } else {
            production("Atr", "EPSILON");
        }
    }

    private void Var() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("Var", "'idVar' OptArray");
            token = lexicalAnalyzer.nextToken();
            OptArray();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
        }
    }

    private void IdFun() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId)))) {
            production("IdFun", "'funId'");
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tmain)))) {
            production("IdFun", "'Main'");
        }
        token = lexicalAnalyzer.nextToken();
    }

    private void ArrSize() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("ArrSize", "'idVar'");
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TcteInt)))) {
            production("ArrSize", "'ConstInt'");
        }
        token = lexicalAnalyzer.nextToken();
    }

    private void VarType() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint)))) {
            production("VarType", "'int'");
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tfloat)))) {
            production("VarType", "'float'");
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tstring)))) {
            production("VarType", "'string'");
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tbool)))) {
            production("VarType", "'bool'");
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tchar)))) {
            production("VarType", "'char'");
        }
        token = lexicalAnalyzer.nextToken();
    }

    private void OpSentCl() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegCrBrac)))) {
            production("OpSentCl", "'{' Sent '}'");
            token = lexicalAnalyzer.nextToken();
            Sent();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendCrBrac)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendCrBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegCrBrac'.");
        }
    }

    private void Param() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            production("Param", "ParamList");
            ParamList();
        } else {
            production("Param", "EPSILON");
        }
    }

    private void ParamList() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            production("ParamList", "ParVar ParamListR");
            ParVar();
            ParamListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'Tint', 'Tfloat', 'Tstring', 'Tbool', 'Tchar'.");
        }
    }

    private void ParamListR() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tcomma)))) {
            production("ParamListR", "',' ParamList");
            token = lexicalAnalyzer.nextToken();
            ParamList();
        } else {
            production("ParamListR", "EPSILON");
        }
    }

    private void ParVar() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            production("ParVar", "VarType OptFnArray 'idVar'");
            VarType();
            OptFnArray();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'Tint', 'Tfloat', 'Tstring', 'Tbool', 'Tchar'.");
        }
    }

    private void OptFnArray() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegSqBrac)))) {
            production("OptFnArray", "'[' OptFnArrayR");
            token = lexicalAnalyzer.nextToken();
            OptFnArrayR();
        } else {
            production("OptFnArray", "EPSILON");
        }
    }

    private void OptFnArrayR() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId, CategoryList.TcteInt)))) {
            production("OptFnArrayR", "ArrSize ']'");
            ArrSize();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
            production("OptFnArrayR", "']'");
            token = lexicalAnalyzer.nextToken();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId', 'TcteInt', 'TendSqBrac'.");
        }
    }

    private void Sent() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)))) {
            production("Sent", "VarType OptArray DeclVar Sent");
            VarType();
            OptArray();
            DeclVar();
            Sent();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId)))) {
            production("Sent", "FunCall ';' Sent");
            FunCall();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                token = lexicalAnalyzer.nextToken();
                Sent();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tprint, CategoryList.Tget, CategoryList.Tif, CategoryList.Tfrom, CategoryList.Tduring)))) {
            production("Sent", "Commands Sent");
            Commands();
            Sent();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("Sent", "Var Atr ';' Sent");
            Var();
            Atr();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                token = lexicalAnalyzer.nextToken();
                Sent();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Treturn)))) {
            production("Sent", "'return' ReturnParam ';' Sent");
            token = lexicalAnalyzer.nextToken();
            ReturnParam();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                token = lexicalAnalyzer.nextToken();
                Sent();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else {
            production("Sent", "EPSILON");
        }
    }

    private void OptArray() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegSqBrac)))) {
            production("OptArray", "'[' ArrSize ']'");
            token = lexicalAnalyzer.nextToken();
            ArrSize();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendSqBrac)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else {
            production("OptArray", "EPSILON");
        }
    }

    private void ReturnParam() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("ReturnParam", "Ec");
            Ec();
        } else {
            production("ReturnParam", "EPSILON");
        }
    }

    private void FunCall() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId)))) {
            production("FunCall", "'idFunc' '(' ParCall ')'");
            token = lexicalAnalyzer.nextToken();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                ParCall();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                    token = lexicalAnalyzer.nextToken();
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TfuncId'.");
        }
    }

    private void ParCall() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("ParCall", "ParCallList");
            ParCallList();
        } else {
            production("ParCall", "EPSILON");
        }
    }

    private void ParCallList() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("ParCallList", "Ec ParCalListR");
            Ec();
            ParCallListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void ParCallListR() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tcomma)))) {
            production("ParCallListR", "',' ParCallList");
            token = lexicalAnalyzer.nextToken();
            ParCallList();
        } else {
            production("ParCallListR", "EPSILON");
        }
    }

    private void Commands() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tprint)))) {
            production("Commands", "'printComm' '(' 'ConstStr' PrintOpt ')' ';'");
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
                            Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
                        }
                    } else {
                        Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
                    }
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'TcteString'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tget)))) {
            production("Commands", "'get' '(' GetVarList ')' ';'");
            token = lexicalAnalyzer.nextToken();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
                token = lexicalAnalyzer.nextToken();
                GetVarList();
                if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                    token = lexicalAnalyzer.nextToken();
                    if(checkToken(new ArrayList<>(List.of(CategoryList.TsemiCol)))) {
                        token = lexicalAnalyzer.nextToken();
                    } else {
                        Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
                    }
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tif)))) {
            production("Commands", "'if' Eb OpSentCl ElseComm");
            token = lexicalAnalyzer.nextToken();
            Eb();
            OpSentCl();
            ElseComm();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tfrom)))) {
            production("Commands", "'from' Var '=' Ea 'to' Ea 'increment' Ea OpSentCl");
            token = lexicalAnalyzer.nextToken();
            Var();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TopAtr)))) {
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
                        Error("Unexpected token '" + token.getId() + "'. Expected 'Tincrement'.");
                    }
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'Tto'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TopAtr'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.Tduring)))) {
            production("Commands", "'during' Eb OpSentCl");
            token = lexicalAnalyzer.nextToken();
            Eb();
            OpSentCl();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'Tprint', 'Tget', 'Tif', 'Tfrom', 'Tduring'.");
        }
    }

    private void PrintOpt() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tcomma)))) {
            production("PrintOpt", "',' ParCallList");
            token = lexicalAnalyzer.nextToken();
            ParCallList();
        } else {
            production("PrintOpt", "EPSILON");
        }
    }

    private void GetVarList() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("GetVarList", "Var GetVarListR");
            Var();
            GetVarListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
        }
    }

    private void GetVarListR() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Tcomma)))) {
            production("GetVarListR", "',' GetVarList");
            token = lexicalAnalyzer.nextToken();
            GetVarList();
        } else {
            production("GetVarListR", "EPSILON");
        }
    }

    private void ElseComm() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.Telse)))) {
            production("ElseComm", "'else' OpSentCl");
            token = lexicalAnalyzer.nextToken();
            OpSentCl();
        } else {
            production("ElseComm", "EPSILON");
        }
    }

    private void Ec() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Ec", "Eb Ecr");
            Eb();
            Ecr();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ecr() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopConc)))) {
            production("Ec", "'&' Eb Ecr");
            token = lexicalAnalyzer.nextToken();
            Eb();
            Ecr();
        } else {
            production("Ecr", "EPSILON");
        }
    }

    private void Eb() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Eb", "Tb Ebr");
            Tb();
            Ebr();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ebr() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopOr)))) {
            production("Ebr", "'LogicOr' Tb Ebr");
            token = lexicalAnalyzer.nextToken();
            Tb();
            Ebr();
        } else {
            production("Ebr", "EPSILON");
        }
    }

    private void Tb() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Tb", "Fb Tbr");
            Fb();
            Tbr();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Tbr() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopAnd)))) {
            production("Tbr", "'LogicAnd' Fb Tbr");
            token = lexicalAnalyzer.nextToken();
            Fb();
            Tbr();
        } else {
            production("Tbr", "EPSILON");
        }
    }

    private void Fb() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Fb", "Fc Fbr");
            Fc();
            Fbr();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TopNot)))) {
            production("Fb", "'LogicNot' Fb");
            token = lexicalAnalyzer.nextToken();
            Fb();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TcteBool)))) {
            production("Fb", "'ConstBool'");
            token = lexicalAnalyzer.nextToken();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Fbr() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopLowThen, CategoryList.TopLowThnE, CategoryList.TopGreThen, CategoryList.TopGreThnE)))) {
            production("Fbr", "'RelLtGt' Fc Fbr");
            token = lexicalAnalyzer.nextToken();
            Fc();
            Fbr();
        } else {
            production("Fbr", "EPSILON");
        }
    }

    private void Fc() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TcteString)))) {
            production("Fc", "'stringCons'");
            token = lexicalAnalyzer.nextToken();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TcteChar)))) {
            production("Fc", "'charCons'");
            token = lexicalAnalyzer.nextToken();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Fc", "Ra");
            Ra();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ra() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Ra", "Ea Rar");
            Ea();
            Rar();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Rar() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopEq, CategoryList.TopDif)))) {
            production("Rar", "'RelEq' Ea Rar");
            token = lexicalAnalyzer.nextToken();
            Ea();
            Rar();
        } else {
            production("Rar", "EPSILON");
        }
    }

    private void Ea() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Ea", "Ta Ear");
            Ta();
            Ear();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ear() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopAdd, CategoryList.TopSub)))) {
            production("Ear", "'AddSub' Ta Ear");
            token = lexicalAnalyzer.nextToken();
            Ta();
            Ear();
        } else {
            production("Ear", "EPSILON");
        }
    }

    private void Ta() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)))) {
            production("Ta", "Fa Tar");
            Fa();
            Tar();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Tar() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TopMult, CategoryList.TopDiv)))) {
            production("Tar", "'Mult' Fa Tarr");
            token = lexicalAnalyzer.nextToken();
            Fa();
            Tar();
        } else {
            production("Tar", "EPSILON");
        }
    }

    private void Fa() {
        if(checkToken(new ArrayList<>(List.of(CategoryList.TbegBrac)))) {
            production("Fa", "'(' Eb ')'");
            token = lexicalAnalyzer.nextToken();
            Eb();
            if(checkToken(new ArrayList<>(List.of(CategoryList.TendBrac)))) {
                token = lexicalAnalyzer.nextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
            }
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TopSub)))) {
            production("Fa", "'UnNeg' Fa");
            token = lexicalAnalyzer.nextToken();
            Fa();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TnameId)))) {
            production("Fa", "Var");
            Var();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TfuncId)))) {
            production("Fa", "FunCall");
            FunCall();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TcteInt)))) {
            production("Fa", "'ConstInt'");
            token = lexicalAnalyzer.nextToken();
        } else if(checkToken(new ArrayList<>(List.of(CategoryList.TcteFloat)))) {
            production("Fa", "'ConstFloat'");
            token = lexicalAnalyzer.nextToken();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

}
