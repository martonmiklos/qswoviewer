/*     */ package com.atollic.truestudio.swv.itm_trace.ui;
/*     */ 
/*     */ import com.atollic.truestudio.oss.resources.SWTResourceManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.DemoHelper;
/*     */ import com.atollic.truestudio.productmanager.exposed.ProductManager;
/*     */ import com.atollic.truestudio.productmanager.exposed.TSFeature;
/*     */ import com.atollic.truestudio.swv.core.SWVBuffer;
/*     */ import com.atollic.truestudio.swv.core.SWVClient;
/*     */ import com.atollic.truestudio.swv.core.SWVPlugin;
/*     */ import com.atollic.truestudio.swv.core.SessionManager;
/*     */ import com.atollic.truestudio.swv.core.ui.SWVView;
/*     */ import com.atollic.truestudio.swv.itm_trace.Activator;
/*     */ import com.atollic.truestudio.swv.model.ITMPortEvent;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.debug.core.DebugPlugin;
/*     */ import org.eclipse.debug.core.ILaunchConfiguration;
/*     */ import org.eclipse.debug.core.ILaunchConfigurationListener;
/*     */ import org.eclipse.debug.core.ILaunchManager;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.action.Separator;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.custom.CTabFolder;
/*     */ import org.eclipse.swt.custom.CTabFolder2Adapter;
/*     */ import org.eclipse.swt.custom.CTabFolderEvent;
/*     */ import org.eclipse.swt.custom.CTabItem;
/*     */ import org.eclipse.swt.custom.StyledText;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.IViewSite;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class SWVConsole extends SWVView
/*     */   implements ILaunchConfigurationListener
/*     */ {
/*     */   public static final int NUMBER_OF_ITM_PORTS = 32;
/*  55 */   private int indexCtr = 0;
/*  56 */   private boolean autoScroll = true;
/*     */   private Map<Integer, ConfiguredPort> ports;
/*  58 */   private SWVClient currentClient = null;
/*  59 */   private boolean viewDisposed = false;
/*     */   private boolean[] launchConfigITM;
/*     */   private Object launchConfigITMLock;
/*     */   private CTabFolder tabFolder;
/*     */   private Action actionAddPort;
/*     */   private Action actionClearConsole;
/*     */   private Action actionScrollLock;
/*  69 */   private final boolean hp = ProductManager.hasPermission(TSFeature.SWV_CONSOLE);
/*     */ 
/*     */   public SWVConsole()
/*     */   {
/* 122 */     this.launchConfigITMLock = new Object();
/*     */   }
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/* 134 */     super.createPartControl(parent);
/* 135 */     Composite composite = new Composite(parent, 0);
/* 136 */     composite.setLayout(new GridLayout(1, false));
/*     */ 
/* 138 */     if (!this.hp) {
/* 139 */       DemoHelper.createDemoModeLabel(composite, "swv-itm-sw-trace");
/*     */     }
/*     */ 
/* 142 */     this.tabFolder = new CTabFolder(composite, 8390656);
/* 143 */     this.tabFolder.setSimple(false);
/* 144 */     GridData gd_tabFolder = new GridData(4, 4, true, true, 1, 1);
/* 145 */     gd_tabFolder.widthHint = 475;
/* 146 */     gd_tabFolder.heightHint = 355;
/* 147 */     this.tabFolder.setLayoutData(gd_tabFolder);
/* 148 */     this.tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(35));
/*     */ 
/* 150 */     this.tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter()
/*     */     {
/*     */       public void close(CTabFolderEvent event)
/*     */       {
/* 154 */         if ((event.item instanceof CTabItem)) {
/* 155 */           CTabItem tabItem = (CTabItem)event.item;
/*     */           try
/*     */           {
/* 158 */             Integer portNumber = (Integer)tabItem.getData();
/* 159 */             SWVConsole.this.ports.remove(portNumber);
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/* 169 */     createActions();
/*     */ 
/* 172 */     this.ports = new HashMap();
/* 173 */     if (isPortEnabled(0)) {
/* 174 */       addPort(0);
/*     */     }
/*     */ 
/* 178 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 179 */     this.currentClient = sessionManager.getClient();
/*     */ 
/* 181 */     if (this.currentClient != null) {
/* 182 */       handleSWVContext();
/*     */     }
/*     */     else {
/* 185 */       handleNoSWVContext();
/* 186 */       addPort(0);
/* 187 */       this.tabFolder.setSelection(0);
/*     */     }
/*     */ 
/* 191 */     DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(this);
/*     */ 
/* 194 */     startPeriodicUpdate(1000);
/*     */   }
/*     */ 
/*     */   private void createActions()
/*     */   {
/* 201 */     this.actionAddPort = new AddPortAction(this);
/* 202 */     this.actionClearConsole = new ClearConsoleAction();
/* 203 */     this.actionScrollLock = new ScrollLockAction(this);
/*     */ 
/* 205 */     IActionBars bars = getViewSite().getActionBars();
/* 206 */     IToolBarManager toolBarManager = bars.getToolBarManager();
/* 207 */     toolBarManager.add(new Separator());
/* 208 */     toolBarManager.add(this.actionClearConsole);
/* 209 */     toolBarManager.add(this.actionScrollLock);
/* 210 */     toolBarManager.add(new Separator());
/* 211 */     toolBarManager.add(this.actionAddPort);
/*     */   }
/*     */ 
/*     */   private synchronized void updateData(SWVBuffer rxBuffer)
/*     */   {
/* 222 */     Object[] records = rxBuffer.getRecords();
/*     */ 
/* 225 */     if ((records == null) || (this.indexCtr == records.length))
/*     */       return;
/*     */     ITMPortEvent event;
/* 230 */     for (int index = this.indexCtr; index < records.length; index++) {
/* 231 */       if ((records[index] instanceof ITMPortEvent)) {
/* 232 */         event = (ITMPortEvent)records[index];
/* 233 */         if (isPortEnabled(event.getPortNumber()))
/*     */         {
/* 235 */           if (isPortConfigured(event.getPortNumber())) {
/* 236 */             ConfiguredPort port = (ConfiguredPort)this.ports.get(Integer.valueOf(event.getPortNumber()));
/*     */ 
/* 238 */             if ((this.hp) || ((char)(int)event.getData() == '\n'))
/*     */             {
/* 240 */               port.append((char)(int)event.getData());
/*     */             }
/*     */             else {
/* 243 */               port.append(".");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 251 */     for (ConfiguredPort port : this.ports.values()) {
/* 252 */       port.flush(this.autoScroll);
/*     */     }
/*     */ 
/* 257 */     this.indexCtr = records.length;
/*     */   }
/*     */ 
/*     */   private boolean isPortEnabled(int port)
/*     */   {
/* 266 */     synchronized (this.launchConfigITMLock) {
/* 267 */       if ((this.launchConfigITM != null) && 
/* 268 */         (port >= 0) && (port < this.launchConfigITM.length)) {
/* 269 */         return this.launchConfigITM[port];
/*     */       }
/*     */     }
/*     */ 
/* 273 */     return false;
/*     */   }
/*     */ 
/*     */   private synchronized void syncPortBuffer(SWVBuffer rxBuffer, ConfiguredPort port) {
/* 277 */     Object[] records = rxBuffer.getRecords();
/*     */ 
/* 280 */     if (records == null) {
/* 281 */       return;
/*     */     }
/*     */ 
/* 284 */     for (int index = 0; index < this.indexCtr; index++) {
/* 285 */       if ((records[index] instanceof ITMPortEvent)) {
/* 286 */         ITMPortEvent event = (ITMPortEvent)records[index];
/* 287 */         if (event.getPortNumber() == port.getItmPortNumber()) {
/* 288 */           port.append((char)(int)event.getData());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 294 */     port.flush(true);
/*     */   }
/*     */ 
/*     */   private void resetAllPorts()
/*     */   {
/* 302 */     this.indexCtr = 0;
/*     */ 
/* 304 */     for (ConfiguredPort port : this.ports.values())
/* 305 */       port.reset();
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 316 */     this.viewDisposed = true;
/* 317 */     SWTResourceManager.disposeImages(Activator.getDefault().getBundle());
/* 318 */     SWTResourceManager.disposeFonts();
/*     */ 
/* 320 */     DebugPlugin.getDefault().getLaunchManager().removeLaunchConfigurationListener(this);
/*     */ 
/* 322 */     super.dispose();
/*     */   }
/*     */ 
/*     */   public void setFocus()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void handleNoSWVContext()
/*     */   {
/* 332 */     this.currentClient = null;
/* 333 */     resetAllPorts();
/*     */   }
/*     */ 
/*     */   public void handleSWVContext()
/*     */   {
/* 339 */     SessionManager sessionManager = SWVPlugin.getDefault().getSessionManager();
/* 340 */     SWVClient traceClient = sessionManager.getClient();
/*     */ 
/* 343 */     if (traceClient != null)
/*     */     {
/* 345 */       if (!traceClient.equals(this.currentClient)) {
/* 346 */         resetAllPorts();
/* 347 */         this.currentClient = traceClient;
/*     */       }
/*     */ 
/* 350 */       readITMConfig(this.currentClient.getLaunchConfiguration());
/* 351 */       openEnabledTabsAndCloseDisabled();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void openEnabledTabsAndCloseDisabled()
/*     */   {
/* 359 */     for (int port = 0; port < 32; port++) {
/* 360 */       Integer portNr = Integer.valueOf(port);
/* 361 */       if (isPortEnabled(port)) {
/* 362 */         addPort(port);
/* 363 */       } else if (isPortConfigured(portNr.intValue())) {
/* 364 */         for (CTabItem item : this.tabFolder.getItems()) {
/* 365 */           Integer itemPortNr = (Integer)item.getData();
/* 366 */           if (portNr.equals(itemPortNr)) {
/* 367 */             item.dispose();
/*     */           }
/*     */         }
/* 370 */         this.ports.remove(portNr);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleClearEvent()
/*     */   {
/* 381 */     super.handleClearEvent();
/* 382 */     resetAllPorts();
/*     */   }
/*     */ 
/*     */   private void readITMConfig(ILaunchConfiguration config)
/*     */   {
/*     */     try
/*     */     {
/* 392 */       String[] values = config.getAttribute("com.atollic.truestudio.swv.core.itmports", "").split(":");
/*     */ 
/* 395 */       synchronized (this.launchConfigITMLock) {
/* 396 */         this.launchConfigITM = new boolean[32];
/* 397 */         for (int i = 0; i < 32; i++)
/* 398 */           if ((values != null) && (values.length == 32))
/* 399 */             this.launchConfigITM[i] = values[i].equals("1");
/*     */           else
/* 401 */             this.launchConfigITM[i] = false;
/*     */       }
/*     */     }
/*     */     catch (CoreException localCoreException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void refreshData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 424 */     if ((this.currentClient != null) && (this.currentClient.isTracing()))
/*     */     {
/* 426 */       updateData(this.currentClient.getRxBuffer());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 432 */     return this.viewDisposed;
/*     */   }
/*     */ 
/*     */   public boolean isPortConfigured(int port) {
/* 436 */     return this.ports.containsKey(Integer.valueOf(port));
/*     */   }
/*     */ 
/*     */   public void addPort(int port)
/*     */   {
/* 445 */     if (isPortConfigured(port)) {
/* 446 */       return;
/*     */     }
/*     */ 
/* 449 */     Integer portNumber = Integer.valueOf(port);
/*     */ 
/* 452 */     StyledText newTextBuff = new StyledText(this.tabFolder, 2634);
/* 453 */     newTextBuff.setBackground(SWTResourceManager.getColor(1));
/* 454 */     Font terminalFont = SWTResourceManager.getFont("Consolas", 10, 0);
/* 455 */     newTextBuff.setFont(terminalFont);
/*     */ 
/* 457 */     CTabItem newTabItem = new CTabItem(this.tabFolder, 0);
/* 458 */     newTabItem.setShowClose(true);
/* 459 */     newTabItem.setText(Messages.bind(Messages.SWVConsole_PORT, portNumber));
/* 460 */     newTabItem.setToolTipText(Messages.bind(Messages.AddPortDialog_ITM_PORT_NUMBER, portNumber));
/* 461 */     newTabItem.setData(portNumber);
/*     */ 
/* 463 */     newTabItem.setControl(newTextBuff);
/*     */ 
/* 466 */     ConfiguredPort configuredPort = new ConfiguredPort(portNumber.intValue(), newTextBuff);
/* 467 */     this.ports.put(portNumber, configuredPort);
/*     */ 
/* 470 */     if (this.currentClient != null) {
/* 471 */       syncPortBuffer(this.currentClient.getRxBuffer(), configuredPort);
/*     */     }
/*     */ 
/* 476 */     if (this.tabFolder.getSelectionIndex() < 0)
/* 477 */       this.tabFolder.setSelection(0);
/*     */   }
/*     */ 
/*     */   public void setAutoScroll(boolean autoScroll)
/*     */   {
/* 486 */     this.autoScroll = autoScroll;
/*     */   }
/*     */ 
/*     */   public void launchConfigurationAdded(ILaunchConfiguration configuration)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void launchConfigurationChanged(ILaunchConfiguration configuration)
/*     */   {
/* 497 */     if ((this.currentClient != null) && (configuration != null)) {
/* 498 */       ILaunchConfiguration config = this.currentClient.getLaunchConfiguration();
/* 499 */       if ((config != null) && 
/* 500 */         (config.equals(configuration))) {
/* 501 */         readITMConfig(config);
/* 502 */         openEnabledTabsAndCloseDisabled();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void launchConfigurationRemoved(ILaunchConfiguration configuration)
/*     */   {
/*     */   }
/*     */ 
/*     */   private class AddPortAction extends Action
/*     */   {
/*     */     private SWVConsole view;
/*     */ 
/*     */     public AddPortAction(SWVConsole view)
/*     */     {
/*  80 */       this.view = view;
/*  81 */       setImageDescriptor(ImageDescriptor.createFromImage(SWTResourceManager.getImage(Activator.getDefault().getBundle(), "add_att.gif")));
/*  82 */       setToolTipText(Messages.ITM_TRACE_UI_ADD_PORT);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*  87 */       new AddPortDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), this.view).open();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ClearConsoleAction extends Action
/*     */   {
/*     */     public ClearConsoleAction()
/*     */     {
/*  99 */       setImageDescriptor(ImageDescriptor.createFromImage(SWTResourceManager.getImage(Activator.getDefault().getBundle(), "clear.gif")));
/* 100 */       setToolTipText(Messages.SWVConsole_CLEAR_CONSOLE);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 105 */       CTabItem tabItem = SWVConsole.this.tabFolder.getSelection();
/*     */ 
/* 107 */       if (tabItem != null) {
/* 108 */         Integer portNr = (Integer)tabItem.getData();
/*     */         try
/*     */         {
/* 111 */           ConfiguredPort portToClear = (ConfiguredPort)SWVConsole.this.ports.get(portNr);
/* 112 */           portToClear.reset();
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /opt/Atollic_TrueSTUDIO_for_STM32_x86_64_9.0.0/ide/plugins/com.atollic.truestudio.swv.itm_trace_1.0.0.20180117-1023.jar
 * Qualified Name:     com.atollic.truestudio.swv.itm_trace.ui.SWVConsole
 * JD-Core Version:    0.6.2
 */