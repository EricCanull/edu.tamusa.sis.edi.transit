//package edu.utexas.sis.edi.transit;
//
//import edu.utexas.sis.edi.model.FileEnvelope;
//import edu.utexas.sis.edi.model.Transcript;
//import edu.utexas.sis.edi.translator.TranslateTranscriptToText;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//import org.apache.log4j.Logger;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.swt.widgets.FileDialog;
//import org.eclipse.ui.IWorkbenchPart;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.plugin.AbstractUIPlugin;
//
//public class GeneratePrintFileAction {
//
//  private FileEnvelope parent;
//  static Logger flogger = Logger.getLogger(GeneratePrintFileAction.class);
//
//  public GeneratePrintFileAction(IWorkbenchWindow window) {
//  }
//
//  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//    setEnabled(false);
//    if (selection instanceof IStructuredSelection) {
//      this.selection = (IStructuredSelection) selection;
//      if (this.selection.getFirstElement() instanceof Transcript) {
//        Transcript t = (Transcript) this.selection.getFirstElement();
//        this.parent = t.getFileEnvelope();
//        setEnabled((this.selection.size() == 1
//                && this.selection.getFirstElement() instanceof Transcript));
//      }
//
//    } else {
//
//      setEnabled(false);
//    }
//  }
//
//
//  public void run() {
//    FileDialog dialog = new FileDialog(this.window.getShell(), '\uFFFD');
//    dialog.setText("Save Print File as:");
//    String[] extension = {".txt"};
//    dialog.setFilterExtensions(extension);
//    File file = new File(dialog.open());
//
//    try {
//      FileOutputStream fileOutStream = new FileOutputStream(file);
//      try (PrintStream pr = new PrintStream(fileOutStream)) {
//        for (int i = 0; i < this.parent.getEduDocs().size(); i++) {
//          
//          if (this.parent.getEduDocs().get(i) instanceof Transcript) {
//            
//            boolean showRap = Activator.getDefault().getPreferenceStore().getBoolean("booleanPreference");
//            boolean overrideInst = Activator.getDefault().getPreferenceStore().getBoolean("booleanOverridePreference");
//            boolean printDuplex = Activator.getDefault().getPreferenceStore().getBoolean("booleanPrintDuplex");
//            String ssnShow = Activator.getDefault().getPreferenceStore().getString("ssnChoicePreference");
//            int lineSize = Activator.getDefault().getPreferenceStore().getInt("lineSizePreference");
//            int pageSize = Activator.getDefault().getPreferenceStore().getInt("pageSizePreference");
//            
//            Transcript transcript = (Transcript) this.parent.getEduDocs().get(i);
//            
//            TranslateTranscriptToText translator = new TranslateTranscriptToText(transcript);
//            translator.setShowRapInfo(showRap);
//            translator.setOverrideInstInfo(overrideInst);
//            translator.setPrintDuplex(printDuplex);
//            translator.setSsnShowInfo(ssnShow);
//            translator.setCharactersPerLine(lineSize);
//            translator.setLinesPerPage(pageSize);
//            transcript.setTranslator(translator);
//            
//            pr.print(transcript.getTranslator().translate());
//          }
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public static void generatePrintFile(String infile, String outfile) {
//    FileEnvelope envelope = new FileEnvelope();
//    try {
//      try (FileInputStream fileInStream = new FileInputStream(infile)) {
//        envelope = new FileEnvelope(fileInStream);
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    try {
//      FileOutputStream fileOutStream = new FileOutputStream(outfile);
//      PrintStream pr = new PrintStream(fileOutStream);
//      for (int i = 0; i < envelope.getEduDocs().size(); i++) {
//
//        if (envelope.getEduDocs().get(i) instanceof Transcript) {
//          boolean showRap = Activator.getDefault().getPreferenceStore().getBoolean("booleanPreference");
//          boolean overrideInst = Activator.getDefault().getPreferenceStore().getBoolean("booleanOverridePreference");
//          boolean printDuplex = Activator.getDefault().getPreferenceStore().getBoolean("booleanPrintDuplex");
//          String ssnShow = Activator.getDefault().getPreferenceStore().getString("ssnChoicePreference");
//          int lineSize = Activator.getDefault().getPreferenceStore().getInt("lineSizePreference");
//          int pageSize = Activator.getDefault().getPreferenceStore().getInt("pageSizePreference");
//
//          Transcript transcript = (Transcript) envelope.getEduDocs().get(i);
//
//          TranslateTranscriptToText translator = new TranslateTranscriptToText(transcript);
//          translator.setShowRapInfo(showRap);
//          translator.setOverrideInstInfo(overrideInst);
//          translator.setPrintDuplex(printDuplex);
//          translator.setSsnShowInfo(ssnShow);
//          translator.setCharactersPerLine(lineSize);
//          translator.setLinesPerPage(pageSize);
//          transcript.setTranslator(translator);
//          pr.print(transcript.getTranslator().translate());
//        }
//      }
//      pr.close();
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    }
//  }
//}
