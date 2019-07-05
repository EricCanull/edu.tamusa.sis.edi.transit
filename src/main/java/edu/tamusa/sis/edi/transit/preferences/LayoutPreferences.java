//package edu.tamusa.sis.edi.transit.preferences;
//
//import edu.utexas.sis.edi.transit.Activator;
//import org.eclipse.jface.preference.BooleanFieldEditor;
//import org.eclipse.jface.preference.FieldEditorPreferencePage;
//import org.eclipse.jface.preference.IntegerFieldEditor;
//import org.eclipse.jface.preference.RadioGroupFieldEditor;
//import org.eclipse.ui.IWorkbench;
//import org.eclipse.ui.IWorkbenchPreferencePage;
//
//public class LayoutPreferences
//        extends FieldEditorPreferencePage
//        implements IWorkbenchPreferencePage {
//
//  public LayoutPreferences() {
//    super(1);
//    setPreferenceStore(Activator.getDefault().getPreferenceStore());
//    setDescription("Allows users to specify layout options:");
//  }
//
//  @Override
//  public void createFieldEditors() {
//    addField(new RadioGroupFieldEditor(
//            "ssnChoicePreference",
//            "Choose how to show student SSN ",
//            1,
//            new String[][]{{"Show all 9 digits", "show"
//            }, {"Show last 4 digits", "show4"},
//            {"Do not show any digit", "noshow"
//            },}, getFieldEditorParent()));
//
//    addField(new BooleanFieldEditor("booleanPreference", "Check to display RAP segments", 1, getFieldEditorParent()));
//    addField(new BooleanFieldEditor("booleanOverridePreference", "HS only - Check to print Override Institute on Course line", 1, getFieldEditorParent()));
//    addField(new BooleanFieldEditor("booleanPrintDuplex", "Duplex Printing - Check to use duplex printing", 1, getFieldEditorParent()));
//
//    IntegerFieldEditor lineSizeIntegerFieldEditor = new IntegerFieldEditor("lineSizePreference", "Enter number of characters per line (Between 78 and 99)", getFieldEditorParent(), 2);
//    lineSizeIntegerFieldEditor.setValidRange(78, 99);
//    addField(lineSizeIntegerFieldEditor);
//
//    IntegerFieldEditor pageSizeIntegerFieldEditor = new IntegerFieldEditor("pageSizePreference", "Enter number of lines per page (Between 20 and 99)", getFieldEditorParent(), 2);
//    pageSizeIntegerFieldEditor.setValidRange(20, 99);
//    addField(pageSizeIntegerFieldEditor);
//
//    IntegerFieldEditor fontHeightIntegerFieldEditor = new IntegerFieldEditor("fontHeightPreference", "Enter height of the display font (Between 10 and 20)", getFieldEditorParent(), 2);
//    fontHeightIntegerFieldEditor.setValidRange(10, 20);
//    addField(fontHeightIntegerFieldEditor);
//
//    IntegerFieldEditor fontWidthIntegerFieldEditor = new IntegerFieldEditor("fontWidthPreference", "Enter width of the display font (Between 8 and 16)", getFieldEditorParent(), 2);
//    fontWidthIntegerFieldEditor.setValidRange(8, 16);
//    addField(fontWidthIntegerFieldEditor);
//  }
//
//  @Override
//  public void init(IWorkbench workbench) {
//  }
//}
