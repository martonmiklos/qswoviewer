/*    */ package com.atollic.truestudio.swv.core.ui.exception;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVInterruptParser;
/*    */ import com.atollic.truestudio.swv.core.SWVInterruptParser.InterruptInfoStatisticsItem;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*    */ import org.eclipse.jface.viewers.Viewer;
/*    */ 
/*    */ public class SWVExceptionStatisticViewContentProvider
/*    */   implements IStructuredContentProvider
/*    */ {
/*    */   public void dispose()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object[] getElements(Object inputElement)
/*    */   {
/* 32 */     if (inputElement == null) {
/* 33 */       System.err.println("ERROR: There is no input element!");
/*    */ 
/* 35 */       return null;
/*    */     }
/* 37 */     if ((inputElement instanceof SWVInterruptParser)) {
/* 38 */       SWVInterruptParser sWVInterruptParser = (SWVInterruptParser)inputElement;
/* 39 */       SWVInterruptParser.InterruptInfoStatisticsItem interruptStatisticalItem = sWVInterruptParser.getInterruptStatistics();
/* 40 */       if (interruptStatisticalItem == null) {
/* 41 */         System.err.println("ERROR: InterruptStatistical item is null!");
/* 42 */         return null;
/*    */       }
/* 44 */       ArrayList iis = interruptStatisticalItem.getInterruptInfos();
/* 45 */       if (iis == null) {
/* 46 */         System.err.println("ERROR: InterruptInfo item is null!");
/* 47 */         return null;
/*    */       }
/* 49 */       Object[] arrayOfObjectsToReturn = iis.toArray();
/* 50 */       return arrayOfObjectsToReturn;
/*    */     }
/*    */ 
/* 53 */     System.err.println("ERROR: inputElement NOT instanceof SWVInterruptParser!");
/* 54 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionStatisticViewContentProvider
 * JD-Core Version:    0.6.2
 */