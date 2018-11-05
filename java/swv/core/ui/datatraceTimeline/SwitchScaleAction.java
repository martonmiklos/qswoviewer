/*    */ package com.atollic.truestudio.swv.core.ui.datatraceTimeline;
/*    */ 
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class SwitchScaleAction extends Action
/*    */ {
/*    */   private SWVDatatraceTimeline view;
/*    */ 
/*    */   public SwitchScaleAction(SWVDatatraceTimeline view)
/*    */   {
/* 13 */     this.view = view;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 18 */     SWVDataTraceChart chart = this.view.getChart();
/* 19 */     if (chart != null)
/* 20 */       chart.switchScale();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatraceTimeline.SwitchScaleAction
 * JD-Core Version:    0.6.2
 */