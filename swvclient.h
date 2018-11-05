#ifndef SWVCLIENT_H
#define SWVCLIENT_H


#include <QLoggingCategory>
#include <QString>
#include <QTcpSocket>
#include <QThread>

#include "itmevent.h"
#include "swvinterruptparser.h"

Q_DECLARE_LOGGING_CATEGORY(swvClient)

class SWVInterruptParser;
class SWVBuffer;

class SWVClient : QThread
{
public:
    SWVClient(/*DsfSession session, ILaunchConfiguration config*/);
    ~SWVClient();

private:
    //SWVComparatorConfig[] comparatorConfig = new SWVComparatorConfig[4];
    QList<ITMEvent*> *rxBuffer;
    QList<ITMEvent*> *exceptionBuffer;
    //DsfSession session;
    bool tracing;
    bool fSuspended;
    QTcpSocket *m_socket = nullptr;
    bool disposed = false;
    QString host;
    int port;
    int swvCoreClock;
    int swvTraceDiv;
    int swoClock;
    int timePrescaler = 1;
    bool TRACE_ON = false;
    int TRACE_TYPE = 6;
    int TRACE_WARNING = 4;
    int TRACE_ERROR = 2;
    int TRACE_MSG = 1;
    quint8 ITMPortPage = 0;
    //ILaunchConfiguration launchConfiguration = nullptr;

    long cycles = 0L;
    quint64 clientCreationTime = 0L;

    int DBGHW_BUFFER_SIZE = 4096;
    bool DBGHW_BUFFER_BOUNDARY_DANGEROUS = false;
    bool WAIT_FOR_SYNC = false;
    int bytesLostDueToSyncWait = 0;

    long totalBytesLost = 0L;

    SWVInterruptParser *sWVInterruptParser = nullptr;
    //static ElfHelper elfHelper = nullptr;

    long overflowPackets = 0L;

    int *buffer;
    long sizeRead = 0L;


    void clear();

    /*DsfSession getSession()
    {
        return session;
    }

    ILaunchConfiguration getLaunchConfiguration()
    {
        return launchConfiguration;
    }*/

    void connect_(QString host, int port, int swoClock, int swvCoreClock, int swvTraceDiv);

    void connect_();


    void setTimePrescaler(int prescaler);

    void setTimestampsEnabled(bool enabled);
    bool timestampsEnabled();

    long getCycles();

    int getSWVTraceDiv() {
        return swvTraceDiv;
    }

    int getSWOScalar();

    int getSWVCoreClock() {
        return swvCoreClock;
    }

    int getSWOClock() {
        return swoClock;
    }

    long getClientCreationTime() {
        return clientCreationTime;
    }

    long getOverflowPacketsCount() {
        return overflowPackets;
    }

    int readByte(bool shouldBeHeader);

    void cleanSocket();

    long getContinuationPacket();

    int getSizeFromSSBits(int ssbits);

    int createProtocolPacket(int c);

    int createInstrumentationPacket(int packetheader);

    int createHardwareSourcePacket(int packetheader);

    void TRACE(int type, QString msg);

    void dispose()
    {
        //interrupt();
        if (isParsingInterrupts()) {
            stopParsingInterrupts();
        }
        /*if (elfHelper != nullptr) {
            delete elfHelper;
            elfHelper = nullptr;
        }*/
    }

    void setTracing(bool tracing)
    {
        tracing = tracing;
    }

    bool isTracing()
    {
        return tracing;
    }

    /*void setComparatorConfig(SWVComparatorConfig config, quint8 cmpId) {
        comparatorConfig[cmpId] = config;
    }

    SWVComparatorConfig getComparatorConfig(quint8 cmpId) {
        return comparatorConfig[cmpId];
    }*/

    bool sessionSuspended()
    {
        return fSuspended;
    }

    void setSuspended(bool suspended)
    {
        fSuspended = suspended;
    }

    bool isParsingInterrupts() {
        if (sWVInterruptParser == nullptr) return false;
        return true;
    }

    SWVInterruptParser* startParsingInterrupts();

    void stopParsingInterrupts()
    {
        delete sWVInterruptParser;
        sWVInterruptParser = nullptr;
    }

    SWVInterruptParser* getInterruptParser();

    /*QString getFunctionName(long addr)
    {
        try
        {
            QString programPath = SWVPlugin.getDefault().getSessionManager().getActiveConfiguration().getAttribute("org.eclipse.cdt.launch.PROGRAM_NAME", "");
            if (programPath != nullptr) {
                programPath = VariablesPlugin.getDefault().getQStringVariableManager().performQStringSubstitution(programPath, true);
            }

            QString projectName = SWVUtil.getActiveProject().getName();
            IPath path = TSProjectManager.getProjectPath(projectName);
            if (path != nullptr)
                programPath = path.append(programPath).toOSQString();
            else {
                return "";
            }

            Elf elf = getElf(programPath);
            if (elf == nullptr) {
                return "";
            }

            Elf.Symbol symbol = elf.getSymbol(new Addr32(addr));
            QString functionName = Messages.SWVStatisticalProfilingView_UNKNOWN_FUNCTION;
            if ((addr >= symbol.st_value.getValue().longValue()) && (addr <= symbol.st_value.getValue().longValue() + symbol.st_size));
            return symbol.toQString() + "()";
        }
        catch (Exception e)
        {
            e->printStackTrace();
        }return Messages.SWVStatisticalProfilingView_UNKNOWN_FUNCTION;
    }*/

    /*static Elf getElf(QString programPath)
    {
        Elf elf = nullptr;
        if (elfHelper == nullptr) {
            try {
                elfHelper = new ElfHelper(programPath);
            } catch (IOException e) {
                e->printStackTrace();
                return nullptr;
            }
        }
        if (elfHelper == nullptr) {
            return nullptr;
        }
        elf = elfHelper.getElf();
        if (elf == nullptr)
            return nullptr;
        try
        {
            elf.loadSymbols();
        } catch (IOException e) {
            e->printStackTrace();
            return nullptr;
        }
        return elf;
    }*/

    void run() override;
private:
    bool m_timeStampsEnabled;
};

#endif // SWVCLIENT_H
