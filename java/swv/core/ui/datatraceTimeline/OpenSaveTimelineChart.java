/*    */ package com.atollic.truestudio.swv.core.ui.datatraceTimeline;
/*    */ 
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class OpenSaveTimelineChart extends Action
/*    */ {
/*    */   private SWVDatatraceTimeline this_time_line;
/*    */ 
/*    */   public OpenSaveTimelineChart(SWVDatatraceTimeline current_time_line)
/*    */   {
/* 10 */     this.this_time_line = current_time_line;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 16 */       SWVDataTraceChart chart = this.this_time_line.getChart();
/* 17 */       if (chart != null)
/* 18 */         chart.openSaveAsDialog();
/*    */     }
/*    */     catch (Exception e) {
/* 21 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.datatraceTimeline.OpenSaveTimelineChart
 * JD-Core Version:    0.6.2
 */