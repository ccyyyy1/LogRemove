package com.gggg.myproc;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class KillStatementProcessor extends AbstractProcessor {

    private int count;
    private Trees trees;
    private TreeMaker make;
    private Name.Table names;
    private List<String> removedList;

    private String currentClass;
    private String currentFun;
    private boolean isNeedRemove;
    private com.sun.tools.javac.util.List<JCTree.JCStatement> needSaveExpList; //like LISP cons = double pointer pair
    boolean isImportLog;
    private TreeTranslator logStatementRecursion;
    private TreeTranslator SimpleViolenceRemove;

    //TODO v2 解决 Log.i("aaa", "bbb" + (v1 = this.findViewById(R.id.tv1)));
    //v1 force remove single statement

    private void log(Object args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, args.toString());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "codeFarmer cyy!");

        if (removeClasss == null || "".equals(removeClasss))
            return true;
//        ImportScanner scanner = new ImportScanner();
//        scanner.scan(roundEnvironment.getRootElements(), null);
//        Set<String> importedTypes = scanner.getImportedTypes();
//        log("importedTypes: " + importedTypes);
//        for (Element e : roundEnvironment.getRootElements()) {
//            System.out.println("FFFF " + e.getSimpleName() + " ---- " + e);
//            for (Modifier m : e.getModifiers()) {
//                System.out.println(m);
//            }
//        }

        if (!roundEnvironment.processingOver()) {
            Set<? extends Element> elements =
                    roundEnvironment.getRootElements();
            for (Element each : elements) {
//                System.out.println("each.getSimpleName(): " + each.getSimpleName() + " ,each.getKind(): " + each.getKind() + " ,each.getEnclosedElements(): " + each.getEnclosedElements());
                if (each.getKind() == ElementKind.CLASS) {
//                    System.out.println("each.getSimpleName(): " + each.getSimpleName());
                    JCTree tree = (JCTree) trees.getTree(each);
                    currentClass = each.toString();
                    TreeTranslator visitor = SimpleViolenceRemove;
                    tree.accept(visitor);
                    currentClass = "";
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();

            int a = removedList.size();
            int i = 1;
            for (String s : removedList) {
                sb.append("[");
                sb.append(i);
                sb.append("/");
                sb.append(a);
                sb.append("] ");
                sb.append(s);
                sb.append("\n");
                i++;
            }

            log(sb.toString());
            log("[ " + count + " ] " + removeClasss + "/" + removeClassSimples + " removed.");
        }

        //true 想要声明的严肃已经被狐狸过了‘
        return false;
    }

    Set<String> removeClasss;
    Set<String> removeClassSimples;

    boolean isRemoveClass(String name) {
        return isRemoveClassFull(name) || isRemoveClassSimple(name);
    }

    boolean isRemoveClassFull(String name) {
        return isMatch(name, removeClasss);
    }

    private boolean isMatch(String name, Set<String> tired) {
//        for (String s : liveSoTired) {
//            if (isMatch(name, s))
//                return true;
//        }
//        return false;
        return tired.contains(name);
    }

    private boolean isMatch(String name, String baseName) {
        return name != null && !"".equals(name) && name.equals(baseName);
    }

    boolean isRemoveClassSimple(String name) {
        return isMatch(name, removeClassSimples);
    }

    //javaCompileOptions {
    //            annotationProcessorOptions {
    //                arguments = [
    //                        MODULE_NAME: "this module name is " + project.getName(),
    //                        REMOVE_LOG_CLASS : "android.util.Log"
    //                ]
    //            }
    //        }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.println("MODULE_NAME is: " + processingEnvironment.getOptions().get("MODULE_NAME"));
        System.out.println("REMOVE_LOG_CLASS is: " + processingEnvironment.getOptions().get("REMOVE_LOG_CLASS"));
        removeClasss = new HashSet<>();
        removeClassSimples = new HashSet<>();

        String removeClass = processingEnvironment.getOptions().get("REMOVE_LOG_CLASS");
        //内部类 com.A$B
        //内部类 com.A.B$C.D
        if (removeClass.contains("$")) {
            throw new RuntimeException("请不要用内部类等奇奇怪怪的东西来充当Log类，暂时不支持复杂情况，如果需要请自行扩展");
        }
        try {
            String[] clzArr = removeClass.split(",");
            for (String rc : clzArr) {
                rc = rc.trim(); // rc .= trim();
                if (!"".equals(rc)) {
                    removeClasss.add(rc);
                    if (rc.lastIndexOf('.') >= 0) {
                        String rcs = rc.substring(rc.lastIndexOf('.') + 1).trim();
                        if (!"".equals(rcs)) {
                            removeClassSimples.add(rcs);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("参数格式填写有误，接受的格式:\"com.a.Log,com.b.Log\"");
        }

        trees = Trees.instance(processingEnvironment);
        Context context = ((JavacProcessingEnvironment) processingEnvironment).getContext();
        make = TreeMaker.instance(context);
//        names = Name.Table.instance(context);
        needSaveExpList = com.sun.tools.javac.util.List.nil();
        removedList = new ArrayList<>();
        count = 0;

        logStatementRecursion = new TreeTranslator() {
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                super.visitClassDef(jcClassDecl);
            }

            @Override
            public void visitTopLevel(JCTree.JCCompilationUnit jcCompilationUnit) {
                System.out.println("jcCompilationUnit = " + jcCompilationUnit);
                super.visitTopLevel(jcCompilationUnit);
            }

            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                currentFun = jcMethodDecl.getName().toString();
                super.visitMethodDef(jcMethodDecl);
                if (jcMethodDecl.name.toString().equals("test1")) {
                    System.out.println("VVVVVVVVVVV  " + jcMethodDecl.name);
                    for (JCTree.JCStatement s : jcMethodDecl.body.stats) {
                        System.out.println(s);
                        System.out.println("s.getKind() " + s.getKind());
                        System.out.println("s.getTag() " + s.getTag());
                        System.out.println("s.getStartPosition() " + s.getStartPosition());
                        System.out.println("s.getTree() " + s.getTree());
                    }
                }
                currentFun = "";
            }

//                        @Override
//                        public void visitBlock(JCTree.JCBlock jcBlock) {
//                            System.out.println("jcBlock = " + jcBlock);
//                            super.visitBlock(jcBlock);
//                        }


            @Override
            public void visitParens(JCTree.JCParens jcParens) {
                super.visitParens(jcParens);
                System.out.println("jcParens = " + jcParens);
            }

            @Override
            public void visitTypeParameter(JCTree.JCTypeParameter jcTypeParameter) {
                super.visitTypeParameter(jcTypeParameter);
                System.out.println("jcTypeParameter = " + jcTypeParameter);
            }

            @Override
            public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
                super.visitSelect(jcFieldAccess);
                System.out.println("jcFieldAccess = " + jcFieldAccess);
            }

            boolean isExecStartTopLevelRemove = false;

            @Override
            public void visitApply(JCTree.JCMethodInvocation mi) {
                boolean isCurrentNeedRemove = isLogMI(mi);
                isNeedRemove |= isCurrentNeedRemove;
                if (isExecStart && isNeedRemove) {
                    isExecStartTopLevelRemove = true;
                }
                isExecStart = false;
                super.visitApply(mi);
                if (isNeedRemove) {
                    if (!isCurrentNeedRemove) {
                        needSaveExpList.add(make.Exec(mi));
                    } else {
//                                    result = make.
//                                    Log.i(); 返回 int
                    }
                }
                System.out.println("jcMethodInvocation = " + mi);
//                            if(isRemoveClass(mi.toString())) {
//                                System.out.println("======================= ");
//                                result = make.Skip();
//                            }
            }

            private boolean isLogMI(JCTree.JCMethodInvocation mi) {
                JCTree.JCExpression ms = mi.getMethodSelect();
                if (ms instanceof JCTree.JCFieldAccess) {
                    JCTree.JCFieldAccess fa = (JCTree.JCFieldAccess) ms;
                    JCTree.JCExpression e = fa.getExpression();
                    if (e instanceof JCTree.JCIdent) {
                        JCTree.JCIdent id = (JCTree.JCIdent) e;
                        String visitClass = id.getName().toString();
                        if (isRemoveClass(visitClass)) { //TODO 符号确定 （用全类名判断)
//                            System.out.println("======================= ");
                            return true;
                        }
                    }
                }
                return false;
            }

            private void yourSdkHaveTooManyLogPlzRemoveIt(JCTree.JCExpressionStatement es) {
                removedList.add("[" + currentClass + " / " + currentFun + "() : " + es.getStartPosition() + "]: " + es.toString() + " ");
                count++;
            }

            boolean isExecStart = false;

            @Override
            public void visitExec(JCTree.JCExpressionStatement es) {
                System.out.println("es = " + es);
                isExecStart = true;
                super.visitExec(es);
                isExecStart = false;
                System.out.println("es = " + es);
                if (isNeedRemove) {
                    yourSdkHaveTooManyLogPlzRemoveIt(es);
                    if (needSaveExpList.size() < 1) {
                        result = make.Skip();
                    } else {
                        make.Block(0, needSaveExpList);
                    }
                    needSaveExpList.clear();
                    isNeedRemove = false;
                }
//                            if(isRemoveClass(es.toString())) {
//                                System.out.println("======================= ");
//                                result = make.Skip();
//                            }
//                            if(es.expr instanceof JCTree.JCPolyExpression){
//                                JCTree.JCPolyExpression pe = (JCTree.JCPolyExpression) es.expr;
//                                System.out.println("JCPolyExpression -------- " + pe.isPoly());
//                            }
//                            if(es.expr instanceof JCTree.JCMethodInvocation){
//                                JCTree.JCMethodInvocation mi = (JCTree.JCMethodInvocation) es.expr;
//                                System.out.println("JCMethodInvocation -------- " + mi.getMethodSelect());
//                            }
//                            System.out.println(es.getKind() + " - " + es.getTag() + " - " + es.getClass());
//                            System.out.println("jcExpressionStatement1 = " + es);
            }

            @Override
            public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
                super.visitVarDef(jcVariableDecl);
            }

            @Override
            public void visitTree(JCTree jcTree) {
                super.visitTree(jcTree);
            }

            @Override
            public void visitAssert(JCTree.JCAssert jcAssert) {
                super.visitAssert(jcAssert);
            }
        };

        SimpleViolenceRemove = new TreeTranslator() {
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                currentFun = jcMethodDecl.getName().toString();
                super.visitMethodDef(jcMethodDecl);
                currentFun = "";
            }

            boolean alertOverDel = false;

            @Override
            public void visitApply(JCTree.JCMethodInvocation mi) {
                boolean isCurrentNeedRemove = isLogMI(mi);
                isNeedRemove |= isCurrentNeedRemove;
                if (isNeedRemove && !isCurrentNeedRemove)
                    alertOverDel = true;
                super.visitApply(mi);
            }

            private boolean isLogMI(JCTree.JCMethodInvocation mi) {
                JCTree.JCExpression ms = mi.getMethodSelect();
                if (ms instanceof JCTree.JCFieldAccess) {
                    JCTree.JCFieldAccess fa = (JCTree.JCFieldAccess) ms;
                    JCTree.JCExpression e = fa.getExpression();
                    if (e instanceof JCTree.JCIdent) {
                        JCTree.JCIdent id = (JCTree.JCIdent) e;
                        String visitClass = id.getName().toString();
                        if (isRemoveClass(visitClass)) { //TODO 符号确定 （用全类名判断)
//                            System.out.println("======================= ");
                            return true;
                        }
                    }
                }
                return false;
            }

            private void yourSdkHaveTooManyLogPlzRemoveIt(JCTree.JCExpressionStatement es) {
                String removeInfo = (alertOverDel ? "(*)" : "   ")
                        + "[" + currentClass + " / " + currentFun + "() : " + "]: " + es.toString();
                alertOverDel = false;
                removedList.add(removeInfo);
                count++;
//                log(count + ") " + removeInfo);
            }

            boolean isExecStart = false;

            @Override
            public void visitExec(JCTree.JCExpressionStatement es) {
                isExecStart = true;
                super.visitExec(es);
                isExecStart = false;
                if (isNeedRemove) {
                    yourSdkHaveTooManyLogPlzRemoveIt(es);
                    result = make.Skip();
                    isNeedRemove = false;
                }
            }
        };
    }

    @Override
    public Set<String> getSupportedOptions() {
//        System.out.println("MyProcessor.getSupportedOptions");
        HashSet<String> set = new HashSet<>();
        set.add("MODULE_NAME");
        set.add("REMOVE_LOG_CLASS");
        return set;
    }
}
