#include "swvclient.h"

#include <QDateTime>

#include "dwtcounterevent.h"
#include "dwtexceptionevent.h"
#include "dwtpcsampleevent.h"
#include "dwtdatatraceaddressoffsetevent.h"
#include "dwtdatatracedatavalueevent.h"
#include "dwtdatatracepcvalueevent.h"

#include "itmextensionevent.h"
#include "itmportevent.h"
#include "itmoverflowevent.h"
#include "itmsyncevent.h"
#include "itmlocaltimestampevent.h"
#include "itmglobaltimestampevent.h"

Q_LOGGING_CATEGORY(swvClient, "SWVClient")

SWVClient::SWVClient()
{
    buffer = new int[102400];
    //session = session;
    clientCreationTime = QDateTime::currentMSecsSinceEpoch();
    //launchConfiguration = config;
    /*try
    {
        DBGHW_BUFFER_BOUNDARY_DANGEROUS = launchConfiguration.getAttribute("com.atollic.hardwaredebug.launch.swv_wait_for_sync", false);
    } catch (Exception e) {
        DBGHW_BUFFER_BOUNDARY_DANGEROUS = false;
        e->printStackTrace();
    }*/
}

SWVClient::~SWVClient()
{
    delete buffer;
}

void SWVClient::clear()
{
    bool clientWasTracing = isTracing();
    setTracing(false);
    rxBuffer->clear();
    exceptionBuffer->clear();
    overflowPackets = 0L;
    cycles = 0L;
    if (clientWasTracing) {
        setTracing(true);
    }
    /*if (elfHelper) {
        delete elfHelper;
        elfHelper = nullptr;
    }*/
}

void SWVClient::connect_(QString host, int port, int swoClock, int swvCoreClock, int swvTraceDiv)
{
    /*if (isAlive()) {
            throw new Exception("SWV Client cannot connect again because thread has already been started.");
        }*/

    host = host;
    port = port;
    swvCoreClock = swvCoreClock;
    swvTraceDiv = swvTraceDiv;
    swoClock = swoClock;

    connect_();

    start();
}

void SWVClient::connect_()
{
    cleanSocket();
    m_socket = new QTcpSocket(this);
}

void SWVClient::setTimePrescaler(int prescaler)
{
    timePrescaler = prescaler;
}

void SWVClient::setTimestampsEnabled(bool enabled)
{
    m_timeStampsEnabled = enabled;
}

bool SWVClient::timestampsEnabled()
{
    return m_timeStampsEnabled;
}

long SWVClient::getCycles()
{
    return cycles;
}

int SWVClient::getSWOScalar()
{
    if (swvTraceDiv < 0) {
        qCDebug(swvClient) << "Bad SWV trace divider, using default.";
        return 128;
    }
    return swvTraceDiv - 1;
}

int SWVClient::readByte(bool shouldBeHeader)
{
    quint8 read = 0;
    if (!m_socket->getChar((char*)&read)) {
        qWarning() << "SWV Client received EOF";
        return -1;
    }

    buffer[((int)sizeRead % 102400)] = read;
    sizeRead++;
    if ((DBGHW_BUFFER_BOUNDARY_DANGEROUS) && (sizeRead != 1L) && ((sizeRead - 1L) % 4096L == 0L)) {
        WAIT_FOR_SYNC = true;
        if (!shouldBeHeader) {
            return -2;
        }
    }
    return read;
}

void SWVClient::cleanSocket()
{

    if (m_socket != nullptr) {
        m_socket->disconnectFromHost();
        m_socket = nullptr;
    }
}

long SWVClient::getContinuationPacket()
{
    long val = 0L;
    int c = 0;

    for (int i = 0; i < 4; i++) {
        c = readByte(false);
        if (c == -2) {
            return -2L;
        }
        if (c == -1) {
            return -1L;
        }
        val |= (c & 0x7F) << 7 * i;

        if ((c & 0x80) == 0)
        {
            return val;
        }
    }
    TRACE(4, "WARNING: continuation packet may be too long, last byte: 0x" + QString::number(c, 16));
    return val;
}

int SWVClient::getSizeFromSSBits(int ssbits)
{
    if (ssbits == 3)
        return 4;
    if ((ssbits > 3) || (ssbits < 1))
        return 0;
    return ssbits;
}

int SWVClient::createProtocolPacket(int c)
{
    TRACE(1, "Protocol packet received, 0x" + QString::number(c, 16));

    if (c == 112) {
        ITMOverflowEvent *e = new ITMOverflowEvent(false);
        rxBuffer->append(e);
        exceptionBuffer->append(e);
        if (isTracing()) {
            overflowPackets += 1L;
        }
        TRACE(1, "OVERFLOW ...\n");
        return 0;
    }

    switch (c & 0xF) {
    case 0: { // TODO create enum for the masks
        if (((c & 0xF0) == 0) || ((c & 0xF0) == 112))
        {
            TRACE(2, "Invalid timestamp packet 0x" + QString::number(c, 16));
            return 1;
        }

        if ((c & 0xC0) == 192)
        {
            quint8 timecontrol = (quint8)((c & 0x30) >> 4);
            long payload = getContinuationPacket();
            if (payload == -1L)
                return -1;
            if (payload == -2L) {
                return -2;
            }
            ITMLocalTimestampEvent *e = new ITMLocalTimestampEvent(timecontrol, payload * timePrescaler);
            if (isTracing()) {
                cycles += e->timestamp();
            }
            e->setCycles(cycles);
            e->setTimes(cycles / swvCoreClock);
            // FIXME rxBuffer.setTimestampOnLastAndInterpolateBack(cycles, cycles / swvCoreClock);
            ITMEvent *event = rxBuffer->last();
            if ((event != nullptr) &&
                    (event->getLocalTimestamp() == nullptr)) {
                event->setLocalTimestamp(e);
            }
        }
        else if ((c & 0x80) == 0)
        {
            long timestamp = (c & 0x70) >> 4;
            ITMLocalTimestampEvent *e = new ITMLocalTimestampEvent((quint8)0, timestamp * timePrescaler);
            if (isTracing()) {
                cycles += e->timestamp();
            }
            e->setCycles(cycles);
            e->setTimes(cycles / swvCoreClock);
            // FIXME implement it rxBuffer.setTimestampOnLastAndInterpolateBack(cycles, cycles / swvCoreClock);
            ITMEvent *event = rxBuffer->last();
            if ((event != nullptr) &&
                    (event->getLocalTimestamp() == nullptr))
                event->setLocalTimestamp(e);
        }
        else
        {
            TRACE(2, "Invalid local timestamp format 0x" + QString::number(c, 16));
        }
    } break;
    case 4: {
        if ((c & 0xDF) == 148)
        {
            long payload = getContinuationPacket();
            if (payload == -1L) {
                return -1;
            }
            if ((c & 0xFF) == 148)
            {
                quint8 clkch = (quint8)(int)((payload & 0x400000) >> 25);
                quint8 wrap = (quint8)(int)((payload & 0x800000) >> 26);
                ITMGlobalTimestampEvent *e = new ITMGlobalTimestampEvent(payload * timePrescaler, clkch, wrap, (quint8)1);
                rxBuffer->append(e);
            }
            else if ((c & 0xFF) == 180)
            {
                ITMGlobalTimestampEvent *e = new ITMGlobalTimestampEvent(payload * timePrescaler, (quint8)0, (quint8)0, (quint8)2);
                rxBuffer->append(e);
            } else {
                TRACE(2, "Invalid global timestamp packet, 0x" + QString::number(c, 16));
            }
        } else {
            TRACE(2, "Invalid global timestamp packet, 0x" + QString::number(c, 16));
            return 1;
        }

    } break;
    case 8: {
        if ((c & 0x80) != 0) {
            TRACE(2, "Invalid ITM extension packet received, 0x" + QString::number(c, 16));
            return 1;
        }
        ITMPortPage = ((quint8)((c & 0x70) >> 4));
        ITMExtensionEvent *e = new ITMExtensionEvent(ITMPortPage);
        rxBuffer->append(e);
    } break;
    default:
        TRACE(4, "WARNING: unknown protocol packet 0x" + QString::number(c, 16));
    }

    return 0;
}

int SWVClient::createInstrumentationPacket(int packetheader)
{
    quint8 port = (quint8)((packetheader & 0xF8) >> 3);
    int size = getSizeFromSSBits(packetheader & 0x3);
    long payload = 0L;

    if (size == 0)
    {
        TRACE(2, "Invalid packet header for this type Instrumentation: 0x" + QString::number(packetheader, 16));
        return 0;
    }

    for (int i = 0; i < size; i++) {
        long c = readByte(false);
        if (c < 0)
            return c;

        payload |= c << 8 * i;
    }
    ITMPortEvent *e = new ITMPortEvent(ITMPortPage, port, payload, size);
    rxBuffer->append(e);

    TRACE(1, QString("Instrumentation packet created, size %1, port %2, payload 0x%3")
          .arg(size)
          .arg(port)
          .arg(QString::number(payload, 16)));

    return 0;
}

void SWVClient::run()
{
    //setName("SWV client thread");

    int c = -1;
    int syncbytes = 0;

    while (!disposed) {
        if (m_socket->bytesAvailable()) {
            c = readByte(true);

            if (-1 == c) {
                break;
            }

            if (c == 0) {
                syncbytes++;
                TRACE(1, "Sync packet received, 0x" + QString::number(c, 16));
            } else {
                if ((syncbytes > 5) || ((syncbytes == 5) && (c == 128))) {
                    TRACE(1, QString("Sync packet complete, size %1, 0x%2").arg(syncbytes).arg(QString::number(c, 16)));

                    if (WAIT_FOR_SYNC) {
                        WAIT_FOR_SYNC = false;
                        totalBytesLost += bytesLostDueToSyncWait;

                        bytesLostDueToSyncWait = 0;

                        ITMOverflowEvent *o = new ITMOverflowEvent(true);
                        rxBuffer->append(o);
                        exceptionBuffer->append(o);
                        if (isTracing()) {
                            overflowPackets += 1L;
                        }
                        TRACE(1, "OVERFLOW DUE TO SYNC...\n");
                    }

                    ITMPortPage = 0;
                    ITMSyncEvent *e = new ITMSyncEvent((syncbytes + 1) * 8);
                    rxBuffer->append(e);

                    syncbytes = 0;

                    int onebits = 0;
                    for (int i = 0; i < 8; i++)
                        if ((c & 1 << i) > 0)
                            onebits++;
                    if (onebits == 1)
                        continue;
                } else if (syncbytes != 0) {
                    TRACE(4, "WARNING: incomplete sync packet discarded, size " + syncbytes);
                    syncbytes = 0;
                }

                if (WAIT_FOR_SYNC) {
                    bytesLostDueToSyncWait += 1;
                } else if ((c & 0x3) == 0) {
                    if (createProtocolPacket(c) == -1)
                        break;
                } else if ((c & 0x3) != 0) {
                    if ((c & 0x4) == 0) {
                        if (createInstrumentationPacket(c) == -1) {
                            break;
                        }
                    } else if (createHardwareSourcePacket(c) == -1) {
                        break;
                    }

                } else {
                    TRACE(2, "Unhandled packet! 0x" + QString::number(c, 16));
                }
            }
        } else {
            QThread::usleep(100); // TODO
        }
    }
}
int SWVClient::createHardwareSourcePacket(int packetheader)
{
    int id = (packetheader & 0xF8) >> 3;
    int size = getSizeFromSSBits(packetheader & 0x3);
    long payload = 0L;

    if (size == 0)
    {
        TRACE(2, "Invalid packet header payload size for this type Hardware source: 0x" + QString::number(packetheader, 16));
        return 0;
    }

    if (id == 0)
    {
        if (size != 1) {
            TRACE(2, "Invalid Event counter packet 0x" + QString::number(packetheader, 16));
            return 1;
        }
    } else if (id == 1)
    {
        if (size != 2) {
            TRACE(2, "Invalid exception tracing packet 0x" + QString::number(packetheader, 16));
            return 1;
        }
    } else if (id == 2)
    {
        TRACE(1, "PC sampling packet");
    } else if ((id >= 8) && (id <= 23))
    {
        quint8 dataPacketType = (quint8)((packetheader & 0xC0) >> 6);
        if (dataPacketType == 1)
        {
            if ((packetheader & 0x8) == 0)
            {
                if (size != 4) {
                    TRACE(2, "Invalid PC value payload size 0x" + QString::number(packetheader, 16));
                    return 1;
                }

            }
            else if (size != 2) {
                TRACE(2, "Invalid Address offset payload size 0x" + QString::number(packetheader, 16));
                return 1;
            }
        }
    }
    else
    {
        TRACE(2, "Invalid hardware source packet id");
        return 1;
    }

    for (int i = 0; i < size; i++)
    {
        long c = readByte(false);
        if (c == -2L) {
            return -2;
        }
        if (c == -1L)
            return -1;
        payload |= c << 8 * i;
    }

    if (id == 0)
    {
        quint8 counters = (quint8)(int)(payload & 0x3F);
        DWTCounterEvent *e = new DWTCounterEvent(counters);
        rxBuffer->append(e);
    }
    else if (id == 1)
    {
        int exceptionNumber = (int)(payload & 0x1FF);
        quint8 function = (quint8)(int)((payload & 0x3000) >> 12);

        if (function == 0) {
            TRACE(4, "WARNING: Reserved function for exception trace packet used 0x00");
        }

        DWTExceptionEvent *e = new DWTExceptionEvent(exceptionNumber, function);
        rxBuffer->append(e);
        exceptionBuffer->append(e);
    }
    else if (id == 2)
    {
        quint8 type = 0;
        if ((size == 1) && (payload == 0L)) {
            type = 1;
        }
        DWTPCSampleEvent *e = new DWTPCSampleEvent(type, payload);
        rxBuffer->append(e);
    } else if ((id >= 8) && (id <= 23))
    {
        quint8 comparator = (quint8)((packetheader & 0x30) >> 4);
        quint8 dataPacketType = (quint8)((packetheader & 0xC0) >> 6);
        if (dataPacketType == 1)
        {
            if ((packetheader & 0x8) == 0)
            {
                DWTDataTracePCValueEvent *e = new DWTDataTracePCValueEvent(comparator, payload);
                rxBuffer->append(e);
            }
            else {
                DWTDataTraceAddressOffsetEvent *e = new DWTDataTraceAddressOffsetEvent(comparator, (int)(payload & 0xFFFF));
                rxBuffer->append(e);
            }
        } else if (dataPacketType == 2)
        {
            quint8 access = (quint8)((packetheader & 0x8) >> 3);
            DWTDataTraceDataValueEvent *e = new DWTDataTraceDataValueEvent(comparator, access, payload);
            rxBuffer->append(e);
        } else {
            TRACE(4, "WARNING: invalid dataPacketType for data trace packet 0x" + QString::number(packetheader, 16));
            return 1;
        }
    }
    else
    {
        TRACE(2, "Invalid hardware source packet id");
    }

    TRACE(1, QString("Hardware source packet created, size %1, id %2, payload 0x%3")
          .arg(size).arg(id).arg(QString::number(payload, 16)));

    return 0;
}

void SWVClient::TRACE(int type, QString msg)
{
    if ((TRACE_ON) &&
            ((TRACE_TYPE & type) > 0))
        qCDebug(swvClient) << QString("listid: (%1) (%2) %3")
                              .arg(sizeRead)
                              .arg(rxBuffer->size())
                              .arg(msg);
}

SWVInterruptParser *SWVClient::startParsingInterrupts()
{
    if (sWVInterruptParser == nullptr) {
        sWVInterruptParser = new SWVInterruptParser(this);
    }

    return sWVInterruptParser;
}

SWVInterruptParser *SWVClient::getInterruptParser()
{
    return sWVInterruptParser;
}
