/*    */ package com.atollic.truestudio.swv.core.ui.exception;
/*    */ 
/*    */ import com.atollic.truestudio.swv.core.SWVClient;
/*    */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*    */ import com.atollic.truestudio.swv.core.SessionManager;
/*    */ import com.atollic.truestudio.swv.model.InterruptInfo;
/*    */ import java.text.DecimalFormat;
/*    */ import org.eclipse.jface.viewers.StyledCellLabelProvider;
/*    */ import org.eclipse.swt.graphics.Color;
/*    */ import org.eclipse.swt.graphics.FontMetrics;
/*    */ import org.eclipse.swt.graphics.GC;
/*    */ import org.eclipse.swt.graphics.Rectangle;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ import org.eclipse.swt.widgets.Event;
/*    */ import org.eclipse.swt.widgets.Table;
/*    */ import org.eclipse.swt.widgets.TableColumn;
/*    */ import org.eclipse.swt.widgets.Widget;
/*    */ 
/*    */ class SWVExceptionStatisticViewStyledLabelProviderForTotalRun extends StyledCellLabelProvider
/*    */   implements SWVExceptionStatisticsViewLabelProviderInterface
/*    */ {
/* 25 */   private final Color colorbg = Display.getDefault().getSystemColor(32);
/*    */ 
/*    */   protected void paint(Event event, Object element)
/*    */   {
/* 29 */     Widget widget = event.widget;
/* 30 */     super.paint(event, element);
/* 31 */     String inuse = null;
/* 32 */     float f = -1.0F;
/* 33 */     if (((element instanceof InterruptInfo)) && (((InterruptInfo)element).getId() != 0))
/*    */     {
/* 35 */       InterruptInfo interruptInfo = (InterruptInfo)element;
/* 36 */       if ((interruptInfo.isAccessed()) && (!interruptInfo.getName().isEmpty())) {
/* 37 */         float totalCykles = (float)getTotalRuntime();
/* 38 */         float totalExceptionRuntime = (float)interruptInfo.getTotalRun();
/* 39 */         if (totalExceptionRuntime > 0.0D) {
/* 40 */           if (totalCykles != 0.0F) {
/* 41 */             f = (float)(100.0D * (totalExceptionRuntime / totalCykles));
/*    */           }
/* 43 */           DecimalFormat dFormat = new DecimalFormat("#.####");
/* 44 */           dFormat.setMinimumFractionDigits(4);
/* 45 */           inuse = dFormat.format(f) + "%";
/*    */ 
/* 47 */           if (f != -1.0F) {
/* 48 */             GC gc = event.gc;
/* 49 */             int width = 0;
/*    */ 
/* 51 */             if ((widget != null) && ((widget instanceof Table))) {
/* 52 */               width = (int)(((Table)widget).getColumn(4).getWidth() * f / 100.0F + 0.5F);
/*    */             } else {
/* 54 */               Rectangle rect = gc.getClipping();
/* 55 */               width = (int)(rect.width * f / 100.0F + 0.5F);
/*    */             }
/* 57 */             int x = event.x;
/* 58 */             int y = event.y;
/* 59 */             int height = event.height;
/*    */ 
/* 61 */             Color oldBackground = gc.getBackground();
/* 62 */             gc.setBackground(this.colorbg);
/* 63 */             gc.fillRectangle(x, y, width, height);
/* 64 */             gc.setBackground(oldBackground);
/*    */ 
/* 67 */             int hdiff = gc.getFontMetrics().getHeight();
/* 68 */             int wdiff = gc.getFontMetrics().getAverageCharWidth();
/* 69 */             hdiff = (int)((height - hdiff) / 2.0F + 0.5F);
/* 70 */             if (hdiff > 0) {
/* 71 */               y += hdiff;
/*    */             }
/* 73 */             gc.drawText(inuse, x + wdiff, y, true);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   private long getTotalRuntime()
/*    */   {
/* 83 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 84 */     if (sessionManager == null)
/* 85 */       return 0L;
/* 86 */     SWVClient traceClient = sessionManager.getClient();
/* 87 */     if ((traceClient == null) || (!traceClient.isParsingInterrupts()))
/* 88 */       return 0L;
/* 89 */     return traceClient.getCycles();
/*    */   }
/*    */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.exception.SWVExceptionStatisticViewStyledLabelProviderForTotalRun
 * JD-Core Version:    0.6.2
 */