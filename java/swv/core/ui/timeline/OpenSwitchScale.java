/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.charts.SWVTimelineChart;
/*    */ import com.atollic.truestudio.swv.core.ui.Messages;
/*    */ import com.atollic.truestudio.swv.model.SWVStatisticalTimelineBuckets;
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class OpenSwitchScale extends Action
/*    */ {
/*    */   private SWVAbstractTimeline this_time_line;
/*    */ 
/*    */   public OpenSwitchScale(SWVAbstractTimeline current_time_line)
/*    */   {
/* 14 */     super(Messages.SHORTER_TIME_STEPS);
/* 15 */     this.this_time_line = current_time_line;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 21 */       SWVTimelineChart chart = this.this_time_line.getViewer();
/* 22 */       if (chart != null) {
/* 23 */         chart.switchScale();
/* 24 */         if ((this.this_time_line.isDebugPaused()) || (this.this_time_line.getCurrentClient() == null)) {
/* 25 */           SWVStatisticalTimelineBuckets buckets = this.this_time_line.getBucketsCache(0L);
/* 26 */           chart.setSeriesFromBuckets(buckets);
/* 27 */           double xMax = buckets.getXMax(chart.getScale());
/* 28 */           chart.setSpan(xMax - chart.getOptimalWidth(), xMax);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 32 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.OpenSwitchScale
 * JD-Core Version:    0.6.2
 */