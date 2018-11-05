/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class OpenSaveTimelineChart extends Action
/*    */ {
/*    */   private SWVAbstractTimeline thisTimeLine;
/*    */ 
/*    */   public OpenSaveTimelineChart(SWVAbstractTimeline current_time_line)
/*    */   {
/* 12 */     this.thisTimeLine = current_time_line;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 18 */       SWVTimelineChart chart = this.thisTimeLine.getViewer();
/* 19 */       if ((chart != null) && (this.thisTimeLine.hasPerm()))
/* 20 */         chart.openSaveAsDialog();
/*    */     }
/*    */     catch (Exception e) {
/* 23 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.OpenSaveTimelineChart
 * JD-Core Version:    0.6.2
 */