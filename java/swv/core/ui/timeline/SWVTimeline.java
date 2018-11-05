/*    */ package com.atollic.truestudio.swv.core.ui.timeline;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.model.SWVStatisticalTimelineBuckets;
/*    */ import org.eclipse.swt.graphics.Color;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ 
/*    */ public class SWVTimeline extends SWVAbstractTimeline
/*    */ {
/*    */   protected Color getGraphColor()
/*    */   {
/* 16 */     return new Color(Display.getDefault(), 147, 136, 0);
/*    */   }
/*    */ 
/*    */   public SWVStatisticalTimelineBuckets getBuckets(long start, long stop)
/*    */   {
/* 26 */     return getCurrentClient().getRxBuffer().createCondensedStatisticalTimeline(start, stop, getCurrentTimeSlot(), getCurrentClient().getSWVCoreClock(), getEventClass());
/*    */   }
/*    */ 
/*    */   public Class getEventClass()
/*    */   {
/* 31 */     return null;
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.timeline.SWVTimeline
 * JD-Core Version:    0.6.2
 */