/*     */ package com.atollic.truestudio.swv.core.ui;
/*     */ 
/*     */ import com.atollic.truestudio.swv.core.ISWVEventListener;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import org.eclipse.core.runtime.FileLocator;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.graphics.ImageData;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.IViewSite;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.handlers.IHandlerService;
/*     */ import org.eclipse.ui.part.ViewPart;
/*     */ import org.osgi.framework.Bundle;
/*     */ 
/*     */ public abstract class SWVView extends ViewPart
/*     */   implements ISWVEventListener
/*     */ {
/*     */   protected static final String EMPTY_STRING = " ";
/*  49 */   private PeriodicUpdate periodicUpdate = null;
/*     */   private Action actionOpenConf;
/*     */   private Image actionOpenConfImage;
/*     */   private Action actionEmptyRxBuffer;
/*     */   private Image actionEmptyRxBufferImage;
/*     */ 
/*     */   public void handleSWVEvent(int event)
/*     */   {
/* 193 */     if (event == 1)
/* 194 */       handleTargetResume();
/* 195 */     else if (event == 0)
/* 196 */       handleTargetSuspended();
/* 197 */     else if (event == 2)
/* 198 */       handleSWVContext();
/* 199 */     else if (event == 3)
/* 200 */       handleNoSWVContext();
/* 201 */     else if (event == 4)
/* 202 */       handleClearEvent();
/*     */     else
/* 204 */       System.out.println("Unknown SWV event " + event + "\n");
/*     */   }
/*     */ 
/*     */   public void handleTargetResume()
/*     */   {
/* 212 */     this.actionOpenConf.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public void handleTargetSuspended()
/*     */   {
/* 219 */     this.actionOpenConf.setEnabled(true);
/*     */   }
/*     */ 
/*     */   public abstract void handleNoSWVContext();
/*     */ 
/*     */   public abstract void handleSWVContext();
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract void refresh();
/*     */ 
/*     */   public abstract void refreshData();
/*     */ 
/*     */   public abstract boolean isDisposed();
/*     */ 
/*     */   protected void startPeriodicUpdate(int freq)
/*     */   {
/* 262 */     stopPeriodicUpdate();
/* 263 */     this.periodicUpdate = new PeriodicUpdate(this, freq);
/* 264 */     this.periodicUpdate.start();
/*     */   }
/*     */ 
/*     */   protected void stopPeriodicUpdate() {
/* 268 */     if (this.periodicUpdate != null)
/*     */       try {
/* 270 */         this.periodicUpdate.interrupt();
/*     */       } catch (SecurityException e) {
/* 272 */         e.printStackTrace();
/*     */       }
/*     */   }
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/* 282 */     createActions();
/*     */ 
/* 285 */     SWVPlugin.getDefault().getSessionManager().addSWVEventListener(this);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 292 */     IActionBars bars = getViewSite().getActionBars();
/* 293 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/*     */ 
/* 295 */     this.actionOpenConf = new OpenConfigAction(null);
/* 296 */     this.actionOpenConfImage = getImage("configure.png");
/* 297 */     this.actionOpenConf.setImageDescriptor(ImageDescriptor.createFromImage(this.actionOpenConfImage));
/* 298 */     this.actionOpenConf.setToolTipText(Messages.CONFIGURE_TOOLBAR_BTN);
/* 299 */     this.actionOpenConf.setId("openConf");
/* 300 */     toolBarManager.add(this.actionOpenConf);
/*     */ 
/* 302 */     this.actionEmptyRxBuffer = new ClearAction(null);
/* 303 */     this.actionEmptyRxBufferImage = getImage("rxBuffer_empty.png");
/* 304 */     this.actionEmptyRxBuffer.setImageDescriptor(ImageDescriptor.createFromImage(this.actionEmptyRxBufferImage));
/* 305 */     this.actionEmptyRxBuffer.setToolTipText(Messages.EMPTY_BUFFER);
/* 306 */     this.actionEmptyRxBuffer.setText(Messages.EMPTY_BUFFER);
/* 307 */     this.actionEmptyRxBuffer.setId("actionEmptyRxBuffer");
/* 308 */     toolBarManager.insertAfter("openConf", this.actionEmptyRxBuffer);
/*     */   }
/*     */ 
/*     */   protected Image getImage(InputStream stream) throws IOException {
/*     */     try {
/* 313 */       Display display = Display.getCurrent();
/* 314 */       ImageData data = new ImageData(stream);
/*     */       Image localImage;
/* 315 */       if (data.transparentPixel > 0) {
/* 316 */         return new Image(display, data, data.getTransparencyMask());
/*     */       }
/* 318 */       return new Image(display, data);
/*     */     } finally {
/* 320 */       stream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Image getImage(String filename) {
/* 325 */     Bundle bundle = SWVPlugin.getDefault().getBundle();
/* 326 */     IPath ipath = new Path("icons/" + filename);
/*     */     try
/*     */     {
/* 329 */       return getImage(FileLocator.openStream(bundle, ipath, false));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 333 */     return null;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 341 */     SWVPlugin.getDefault().getSessionManager().removeSWVEventListener(this);
/* 342 */     this.actionOpenConfImage.dispose();
/* 343 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public SWVClient getCurrentClient() {
/* 347 */     return SWVPlugin.getDefault().getSessionManager().getClient();
/*     */   }
/*     */ 
/*     */   private class ClearAction extends Action
/*     */   {
/*     */     private ClearAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 115 */       SWVPlugin.getDefault().getSessionManager().clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class OpenConfigAction extends Action
/*     */   {
/*     */     private OpenConfigAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*  61 */       SWVClient client = SWVPlugin.getDefault().getSessionManager().getClient();
/*  62 */       boolean isTraceStopped = false;
/*     */ 
/*  64 */       if ((client != null) && 
/*  65 */         (client.isTracing()))
/*     */       {
/*  71 */         IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
/*     */         try {
/*  73 */           handlerService.executeCommand("com.atollic.truestudio.swv.core.start_trace", null);
/*  74 */           isTraceStopped = true;
/*     */         }
/*     */         catch (Exception e) {
/*  77 */           e.printStackTrace();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  90 */       IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
/*     */       try {
/*  92 */         handlerService.executeCommand("com.atollic.truestudio.swv.core.openconfig", null);
/*     */       }
/*     */       catch (Exception e) {
/*  95 */         e.printStackTrace();
/*     */       }
/*  97 */       if ((isTraceStopped) && (handlerService != null))
/*     */         try {
/*  99 */           handlerService.executeCommand("com.atollic.truestudio.swv.core.start_trace", null);
/*     */         }
/*     */         catch (Exception e) {
/* 102 */           e.printStackTrace();
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class PeriodicUpdate extends Thread
/*     */   {
/*     */     SWVView view;
/*     */     int freq;
/*     */ 
/*     */     public PeriodicUpdate(SWVView view, int freq)
/*     */     {
/* 132 */       this.view = view;
/* 133 */       this.freq = freq;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 142 */       while (!this.view.isDisposed())
/*     */         try
/*     */         {
/* 145 */           Thread.sleep(this.freq);
/*     */ 
/* 155 */           SWVView.this.refreshData();
/*     */ 
/* 159 */           Display.getDefault().syncExec(new Runnable()
/*     */           {
/*     */             public void run() {
/* 162 */               if (!SWVView.PeriodicUpdate.this.view.isDisposed())
/* 163 */                 SWVView.PeriodicUpdate.this.view.refresh();
/*     */             }
/*     */           });
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.core_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.core.ui.SWVView
 * JD-Core Version:    0.6.2
 */