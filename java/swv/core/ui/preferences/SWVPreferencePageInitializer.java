/*    */ package com.atollic.truestudio.swv.core.ui.preferences;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
/*    */ import org.eclipse.jface.preference.IPreferenceStore;
/*    */ 
/*    */ public class SWVPreferencePageInitializer extends AbstractPreferenceInitializer
/*    */ {
/*    */   public void initializeDefaultPreferences()
/*    */   {
/* 27 */     IPreferenceStore store = SWVPlugin.getDefault().getPreferenceStore();
/* 28 */     store.setDefault("com.atollic.truestudio.swv.core.pref_buff_size", 2000000);
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.preferences.SWVPreferencePageInitializer
 * JD-Core Version:    0.6.2
 */