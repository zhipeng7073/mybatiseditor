package org.eclipselabs.mybatiseditor.ui.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipselabs.mybatiseditor.ui.MyBatisEditorUiLogger;

@SuppressWarnings("restriction")
public class MyBatisStatementHyperlink extends MyBatisHyperlink {

    private final IFile file;

    private final String statementName;

    private final IDOMNode node;

    public MyBatisStatementHyperlink(IRegion hyperlinkRegion, IFile file, String statementName, IDOMNode node) {
        super(hyperlinkRegion);
        this.file = file;
        this.statementName = statementName;
        this.node = node;
    }

    @Override
    public String getHyperlinkText() {
        return "Go to MyBatis definition of " + statementName;
    }

    @Override
    public void open() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart currentEditor = page.getActiveEditor();
        try {
            IEditorPart editor = findTextEditor(IDE.openEditor(page, file, true));
            page.getNavigationHistory().markLocation(currentEditor);

            if (editor instanceof ITextEditor) {
                ((ITextEditor) editor).selectAndReveal(node.getStartOffset(),
                        node.getEndOffset() - node.getStartOffset());
            }
        } catch (PartInitException e) {
            MyBatisEditorUiLogger.error("Error while opening link", e);
        }
    }
}