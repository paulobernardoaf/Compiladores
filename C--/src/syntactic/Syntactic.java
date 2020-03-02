package syntactic;

import categories.CategoryList;
import lexic.Lexical;
import token.Token;

public class Syntactic {

    private Lexical lexicalAnalyzer;
    public Token token;

    public Syntactic(String args) {
        this.lexicalAnalyzer = new Lexical(args);
    }

    private void Error(String message) {
        System.err.println("Error: " + message + " In line " + token.getLineNumber() + " and column " + token.getColumnNumber());
        System.exit(-1);
    }

    private void production(String left, String right) {
        String format = "%10s%s = %s";
        System.out.println(String.format(format, "", left, right));
    }

    private Token getNextToken() {
        System.out.println(token);
        return lexicalAnalyzer.nextToken();
    }

    private boolean checkToken(CategoryList... categories) {

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

    public void S() {
        if (checkToken(CategoryList.Tint)) {
            production("S", "'int' SR");
            token = getNextToken();
            SR();
        } else if(checkToken(CategoryList.Tfloat, CategoryList.Tchar, CategoryList.Tbool, CategoryList.Tstring)) {
            production("S", "NotIntType Decl S");
            NotIntType();
            Decl();
            S();
        } else if(checkToken(CategoryList.Tvoid)) {
            production("S", "'void' DeclFun S");
            token = getNextToken();
            DeclFun();
            S();
        }

    }

    private void SR() {
        if(checkToken(CategoryList.Tmain)) {
            production("SR", "'Main' '(' Param ')' OpSentCl");
            token = getNextToken();
            if(checkToken(CategoryList.TbegBrac)) {
                token = getNextToken();
                Param();
                if(checkToken(CategoryList.TendBrac)) {
                    token = getNextToken();
                    OpSentCl();
                    if(checkToken(CategoryList.TEOF)) {
                        production("S", "EOF");
                    } else {
                        Error("Unexpected token '" + token.getId() + "'. Expected 'TEOF'.");
                    }
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac'.");
            }
        } else if (checkToken(CategoryList.TbegSqBrac, CategoryList.TfuncId, CategoryList.TnameId)) {
            Decl();
            S();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegSqBrac', 'TfuncId', 'Tmain', 'TnameId'.");
        }
    }

    private void Decl() {
        if(checkToken(CategoryList.TbegSqBrac)) {
            production("Decl", "'[' DeclR");
            token = getNextToken();
            DeclR();
        } else if(checkToken(CategoryList.TfuncId, CategoryList.TnameId)) {
            production("Decl", "DeclAux");
            DeclAux();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegSqBrac', 'TfuncId', 'TnameId'.");
        }

    }

    private void DeclR() {
        if(checkToken(CategoryList.TendSqBrac)) {
            production("DeclR", "']' DeclFun");
            token = getNextToken();
            DeclFun();
        } else if(checkToken(CategoryList.TnameId, CategoryList.TcteInt)){
            production("DeclR", "ArrSize ']' DeclAux");
            ArrSize();
            if(checkToken(CategoryList.TendSqBrac)) {
                token = getNextToken();
                DeclAux();
            }  else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac', 'TnameId', 'TcteInt'.");
        }
    }

    private void DeclAux() {
        if(checkToken(CategoryList.TfuncId)) {
            production("DeclAux", "DeclFun");
            DeclFun();
        } else if(checkToken(CategoryList.TnameId)) {
            production("DeclAux", "DeclVar");
            DeclVar();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TfuncId', 'TnameId'.");
        }

    }

    private void DeclFun() {
        if (checkToken(CategoryList.TfuncId)) {
            production("DeclFun", "'funcId' '(' Param ')' OpSentCl");
            token = getNextToken();
            if(checkToken(CategoryList.TbegBrac)) {
                token = getNextToken();
                Param();
                if(checkToken(CategoryList.TendBrac)) {
                    token = getNextToken();
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
        if(checkToken(CategoryList.TnameId)) {
            production("DeclVar", "'nameId' AtrOpc ';'");
            token = getNextToken();
            AtrOpc();
            if(checkToken(CategoryList.TsemiCol)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
        }
    }

    private void AtrObg() {
        if(checkToken(CategoryList.TopAtr)) {
            production("AtrObg", "'=' AtrR");
            token = getNextToken();
            AtrR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopAtr'.");
        }
    }

    private void AtrOpc() {
        if(checkToken(CategoryList.TopAtr)) {
            production("AtrOpc", "AtrObg");
            AtrObg();
        } else {
            production("AtrOpc", "EPSILON");
        }
    }

    private void AtrR() {
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("AtrR", "Ec");
            Ec();
        } else if(checkToken(CategoryList.TbegSqBrac)) {
            production("AtrR", "'[' EcList ']'");
            token = getNextToken();
            EcList();
            if(checkToken(CategoryList.TendSqBrac)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegSqBrac', 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void EcList() {
        if (checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("EcList", "EcListValues");
            EcListValues();
        } else {
            production("EcList", "EPSILON");
        }
    }

    private void EcListValues() {
        if (checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("EcLisValues", "EcListValues");
            Ec();
            EcListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void EcListR() {
        if(checkToken(CategoryList.Tcomma)) {
            production("EcListR", "',' EcListValues");
            token = getNextToken();
            EcListValues();
        } else {
            production("EcListR", "EPSILON");
        }
    }

    private void Var() {
        if(checkToken(CategoryList.TnameId)) {
            production("Var", "'nameId' OptArray");
            token = getNextToken();
            OptArray();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
        }
    }

    private void ArrSize() {
        if(checkToken(CategoryList.TnameId)) {
            production("ArrSize", "'nameId'");
        } else if(checkToken(CategoryList.TcteInt)) {
            production("ArrSize", "'cteInt'");
        }
        token = getNextToken();
    }

    private void NotIntType() {
        if(checkToken(CategoryList.Tfloat)) {
            production("NotIntType", "'float'");
        } else if(checkToken(CategoryList.Tstring)) {
            production("NotIntType", "'string'");
        } else if(checkToken(CategoryList.Tbool)) {
            production("NotIntType", "'bool'");
        } else if(checkToken(CategoryList.Tchar)) {
            production("NotIntType", "'char'");
        }
        token = getNextToken();
    }

    private void VarType() {
        if(checkToken(CategoryList.Tint)) {
            production("VarType", "'int'");
        } else if(checkToken(CategoryList.Tfloat)) {
            production("VarType", "'float'");
        } else if(checkToken(CategoryList.Tstring)) {
            production("VarType", "'string'");
        } else if(checkToken(CategoryList.Tbool)) {
            production("VarType", "'bool'");
        } else if(checkToken(CategoryList.Tchar)) {
            production("VarType", "'char'");
        }
        token = getNextToken();
    }

    private void OpSentCl() {
        if(checkToken(CategoryList.TbegCrBrac)) {
            production("OpSentCl", "'{' Sent '}'");
            token = getNextToken();
            Sent();
            if(checkToken(CategoryList.TendCrBrac)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendCrBrac'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegCrBrac'.");
        }
    }

    private void Param() {
        if(checkToken(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)) {
            production("Param", "ParamList");
            ParamList();
        } else {
            production("Param", "EPSILON");
        }
    }

    private void ParamList() {
        if(checkToken(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)) {
            production("ParamList", "ParVar ParamListR");
            ParVar();
            ParamListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'Tint', 'Tfloat', 'Tstring', 'Tbool', 'Tchar'.");
        }
    }

    private void ParamListR() {
        if(checkToken(CategoryList.Tcomma)) {
            production("ParamListR", "',' ParamList");
            token = getNextToken();
            ParamList();
        } else {
            production("ParamListR", "EPSILON");
        }
    }

    private void ParVar() {
        if(checkToken(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)) {
            production("ParVar", "VarType OptFnArray 'nameId'");
            VarType();
            OptFnArray();
            if(checkToken(CategoryList.TnameId)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
            }
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'Tint', 'Tfloat', 'Tstring', 'Tbool', 'Tchar'.");
        }
    }

    private void OptFnArray() {
        if(checkToken(CategoryList.TbegSqBrac)) {
            production("OptFnArray", "'[' OptFnArrayR");
            token = getNextToken();
            OptFnArrayR();
        } else {
            production("OptFnArray", "EPSILON");
        }
    }

    private void OptFnArrayR() {
        if(checkToken(CategoryList.TnameId, CategoryList.TcteInt)) {
            production("OptFnArrayR", "ArrSize ']'");
            ArrSize();
            if(checkToken(CategoryList.TendSqBrac)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else if(checkToken(CategoryList.TendSqBrac)) {
            production("OptFnArrayR", "']'");
            token = getNextToken();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId', 'TcteInt', 'TendSqBrac'.");
        }
    }

    private void Sent() {
        if(checkToken(CategoryList.Tint, CategoryList.Tfloat, CategoryList.Tstring, CategoryList.Tbool, CategoryList.Tchar)) {
            production("Sent", "VarType OptArray DeclVar Sent");
            VarType();
            OptArray();
            DeclVar();
            Sent();
        } else if(checkToken(CategoryList.TfuncId)) {
            production("Sent", "FunCall ';' Sent");
            FunCall();
            if(checkToken(CategoryList.TsemiCol)) {
                token = getNextToken();
                Sent();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else if(checkToken(CategoryList.Tprint, CategoryList.Tget, CategoryList.Tif, CategoryList.Tfrom, CategoryList.Tduring)) {
            production("Sent", "Commands Sent");
            Commands();
            Sent();
        } else if(checkToken(CategoryList.TnameId)) {
            production("Sent", "Var AtrObg ';' Sent");
            Var();
            AtrObg();
            if(checkToken(CategoryList.TsemiCol)) {
                token = getNextToken();
                Sent();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else if(checkToken(CategoryList.Treturn)) {
            production("Sent", "'return' ReturnParam ';' Sent");
            token = getNextToken();
            ReturnParam();
            if(checkToken(CategoryList.TsemiCol)) {
                token = getNextToken();
                Sent();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
            }
        } else {
            production("Sent", "EPSILON");
        }
    }

    private void OptArray() {
        if(checkToken(CategoryList.TbegSqBrac)) {
            production("OptArray", "'[' ArrSize ']'");
            token = getNextToken();
            ArrSize();
            if(checkToken(CategoryList.TendSqBrac)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else {
            production("OptArray", "EPSILON");
        }
    }

    private void ReturnParam() {
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("ReturnParam", "Ec");
            Ec();
        } else if(checkToken(CategoryList.TbegSqBrac)) {
            token = getNextToken();
            EcList();
            if(checkToken(CategoryList.TendSqBrac)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendSqBrac'.");
            }
        } else {
            production("ReturnParam", "EPSILON");
        }
    }

    private void FunCall() {
        if(checkToken(CategoryList.TfuncId)) {
            production("FunCall", "'funcId' '(' ParCall ')'");
            token = getNextToken();
            if(checkToken(CategoryList.TbegBrac)) {
                token = getNextToken();
                ParCall();
                if(checkToken(CategoryList.TendBrac)) {
                    token = getNextToken();
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
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("ParCall", "ParCallList");
            ParCallList();
        } else {
            production("ParCall", "EPSILON");
        }
    }

    private void ParCallList() {
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("ParCallList", "Ec ParCalListR");
            Ec();
            ParCallListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void ParCallListR() {
        if(checkToken(CategoryList.Tcomma)) {
            production("ParCallListR", "',' ParCallList");
            token = getNextToken();
            ParCallList();
        } else {
            production("ParCallListR", "EPSILON");
        }
    }

    private void Commands() {
        if(checkToken(CategoryList.Tprint)) {
            production("Commands", "'printComm' '(' 'cteString' PrintOpt ')' ';'");
            token = getNextToken();
            if(checkToken(CategoryList.TbegBrac)) {
                token = getNextToken();
                if(checkToken(CategoryList.TcteString)) {
                    token = getNextToken();
                    PrintOpt();
                    if(checkToken(CategoryList.TendBrac)) {
                        token = getNextToken();
                        if(checkToken(CategoryList.TsemiCol)) {
                            token = getNextToken();
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
        } else if(checkToken(CategoryList.Tget)) {
            production("Commands", "'get' '(' GetVarList ')' ';'");
            token = getNextToken();
            if(checkToken(CategoryList.TbegBrac)) {
                token = getNextToken();
                GetVarList();
                if(checkToken(CategoryList.TendBrac)) {
                    token = getNextToken();
                    if(checkToken(CategoryList.TsemiCol)) {
                        token = getNextToken();
                    } else {
                        Error("Unexpected token '" + token.getId() + "'. Expected 'TsemiCol'.");
                    }
                } else {
                    Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
                }
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac'.");
            }
        } else if(checkToken(CategoryList.Tif)) {
            production("Commands", "'if' Eb OpSentCl ElseComm");
            token = getNextToken();
            Eb();
            OpSentCl();
            ElseComm();
        } else if(checkToken(CategoryList.Tfrom)) {
            production("Commands", "'from' Var '=' Ea 'to' Ea 'increment' Ea OpSentCl");
            token = getNextToken();
            Var();
            if(checkToken(CategoryList.TopAtr)) {
                token = getNextToken();
                Ea();
                if(checkToken(CategoryList.Tto)) {
                    token = getNextToken();
                    Ea();
                    if(checkToken(CategoryList.Tincrement)) {
                        token = getNextToken();
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
        } else if(checkToken(CategoryList.Tduring)) {
            production("Commands", "'during' Eb OpSentCl");
            token = getNextToken();
            Eb();
            OpSentCl();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'Tprint', 'Tget', 'Tif', 'Tfrom', 'Tduring'.");
        }
    }

    private void PrintOpt() {
        if(checkToken(CategoryList.Tcomma)) {
            production("PrintOpt", "',' ParCallList");
            token = getNextToken();
            ParCallList();
        } else {
            production("PrintOpt", "EPSILON");
        }
    }

    private void GetVarList() {
        if(checkToken(CategoryList.TnameId)) {
            production("GetVarList", "Var GetVarListR");
            Var();
            GetVarListR();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TnameId'.");
        }
    }

    private void GetVarListR() {
        if(checkToken(CategoryList.Tcomma)) {
            production("GetVarListR", "',' GetVarList");
            token = getNextToken();
            GetVarList();
        } else {
            production("GetVarListR", "EPSILON");
        }
    }

    private void ElseComm() {
        if(checkToken(CategoryList.Telse)) {
            production("ElseComm", "'else' OpSentCl");
            token = getNextToken();
            OpSentCl();
        } else {
            production("ElseComm", "EPSILON");
        }
    }

    private void Ec() {
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Ec", "Eb Ecr");
            Eb();
            Ecr();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ecr() {
        if(checkToken(CategoryList.TopConc)) {
            production("Ec", "'&' Eb Ecr");
            token = getNextToken();
            Eb();
            Ecr();
        } else {
            production("Ecr", "EPSILON");
        }
    }

    private void Eb() {
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Eb", "Tb Ebr");
            Tb();
            Ebr();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ebr() {
        if(checkToken(CategoryList.TopOr)) {
            production("Ebr", "'opOr' Tb Ebr");
            token = getNextToken();
            Tb();
            Ebr();
        } else {
            production("Ebr", "EPSILON");
        }
    }

    private void Tb() {
        if(checkToken(CategoryList.TopNot, CategoryList.TcteBool, CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Tb", "Fb Tbr");
            Fb();
            Tbr();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Tbr() {
        if(checkToken(CategoryList.TopAnd)) {
            production("Tbr", "'opAnd' Fb Tbr");
            token = getNextToken();
            Fb();
            Tbr();
        } else {
            production("Tbr", "EPSILON");
        }
    }

    private void Fb() {
        if(checkToken(CategoryList.TcteString, CategoryList.TcteChar, CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Fb", "Fc Fbr");
            Fc();
            Fbr();
        } else if(checkToken(CategoryList.TopNot)) {
            production("Fb", "'opNot' Fb");
            token = getNextToken();
            Fb();
        } else if(checkToken(CategoryList.TcteBool)) {
            production("Fb", "'cteBool'");
            token = getNextToken();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TopNot', 'TcteBool', 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Fbr() {
        if(checkToken(CategoryList.TopLowThen, CategoryList.TopLowThnE, CategoryList.TopGreThen, CategoryList.TopGreThnE)) {
            production("Fbr", "'RelLtGt' Fc Fbr");
            token = getNextToken();
            Fc();
            Fbr();
        } else {
            production("Fbr", "EPSILON");
        }
    }

    private void Fc() {
        if(checkToken(CategoryList.TcteString)) {
            production("Fc", "'cteString'");
            token = getNextToken();
        } else if(checkToken(CategoryList.TcteChar)) {
            production("Fc", "'cteChar'");
            token = getNextToken();
        } else if(checkToken(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Fc", "Ra");
            Ra();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TcteString', 'TcteChar', 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ra() {
        if(checkToken(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Ra", "Ea Rar");
            Ea();
            Rar();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Rar() {
        if(checkToken(CategoryList.TopEq, CategoryList.TopDif)) {
            production("Rar", "'opEq' Ea Rar");
            token = getNextToken();
            Ea();
            Rar();
        } else {
            production("Rar", "EPSILON");
        }
    }

    private void Ea() {
        if(checkToken(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Ea", "Ta Ear");
            Ta();
            Ear();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Ear() {
        if(checkToken(CategoryList.TopAdd, CategoryList.TopSub)) {
            production("Ear", "'AddSub' Ta Ear");
            token = getNextToken();
            Ta();
            Ear();
        } else {
            production("Ear", "EPSILON");
        }
    }

    private void Ta() {
        if(checkToken(CategoryList.TbegBrac, CategoryList.TopSub, CategoryList.TnameId, CategoryList.TfuncId, CategoryList.TcteInt, CategoryList.TcteFloat)) {
            production("Ta", "Fa Tar");
            Fa();
            Tar();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

    private void Tar() {
        if(checkToken(CategoryList.TopMult, CategoryList.TopDiv)) {
            production("Tar", "'Mult' Fa Tarr");
            token = getNextToken();
            Fa();
            Tar();
        } else {
            production("Tar", "EPSILON");
        }
    }

    private void Fa() {
        if(checkToken(CategoryList.TbegBrac)) {
            production("Fa", "'(' Ec ')'");
            token = getNextToken();
            Ec();
            if(checkToken(CategoryList.TendBrac)) {
                token = getNextToken();
            } else {
                Error("Unexpected token '" + token.getId() + "'. Expected 'TendBrac'.");
            }
        } else if(checkToken(CategoryList.TopSub)) {
            production("Fa", "'UnNeg' Fa");
            token = getNextToken();
            Fa();
        } else if(checkToken(CategoryList.TnameId)) {
            production("Fa", "Var");
            Var();
        } else if(checkToken(CategoryList.TfuncId)) {
            production("Fa", "FunCall");
            FunCall();
        } else if(checkToken(CategoryList.TcteInt)) {
            production("Fa", "'cteInt'");
            token = getNextToken();
        } else if(checkToken(CategoryList.TcteFloat)) {
            production("Fa", "'cteFloat'");
            token = getNextToken();
        } else {
            Error("Unexpected token '" + token.getId() + "'. Expected 'TbegBrac', 'TopSub', 'TnameId', 'TfuncId', 'TcteInt', 'TcteFloat'.");
        }
    }

}