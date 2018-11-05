#include "itmevent.h"

#include <QDateTime>

#include "itmlocaltimestampevent.h"

ITMEvent::ITMEvent()
{
    pcTimestamp = QDateTime::currentMSecsSinceEpoch();
}

QString ITMEvent::getEventsText(QList<ITMEvent *> eventList)
{
    QString result = "";
    if (eventList.length() > 41) {
        return "";
    }
    for (int i = 1; i < eventList.length(); i++) { // TODO STL style is faster
        ITMEvent *event = eventList[i];
        if (event == nullptr) {
            break;
        }
        result += QString("%1 | %2 | %3")
                .arg(event->getEventID())
                .arg(event->printType())
                .arg(event->printData());
        result += QString("%1 | %2 | %3")
                .arg(event->getEventID())
                .arg(event->printType())
                .arg(event->printExtraInfo());
    }
    return result;
}

bool ITMEvent::isCyclesValueIsNotReal()
{
    return cyclesValueIsNotReal;
}

void ITMEvent::setCyclesValueIsNotReal(bool cyclesValueIsNotReal)
{
    cyclesValueIsNotReal = cyclesValueIsNotReal;
}

long ITMEvent::getCycles()
{
    return cycles;
}

void ITMEvent::setCycles(long cycles)
{
    cycles = cycles;
}

double ITMEvent::getTimes()
{
    return times;
}

void ITMEvent::setTimes(double times)
{
    times = times;
}

long ITMEvent::getEventID()
{
    return eventID;
}

void ITMEvent::setEventID(long eventID)
{
    eventID = eventID;
}

void ITMEvent::setEventType(quint8 eventType) {
    eventType = eventType;
}

quint8 ITMEvent::getEventType() {
    return eventType;
}

long ITMEvent::getPcTimestamp()
{
    return pcTimestamp;
}

void ITMEvent::setPcTimestamp(long pcTimestamp)
{
    pcTimestamp = pcTimestamp;
}

ITMLocalTimestampEvent *ITMEvent::getLocalTimestamp()
{
    return localTimestamp;
}

void ITMEvent::setLocalTimestamp(ITMLocalTimestampEvent *timestampEvent)
{
    if (localTimestamp != nullptr) {
        cycles = timestampEvent->getCycles();
        times = timestampEvent->getTimes();
    }
    localTimestamp = timestampEvent;
}

QString ITMEvent::printType()
{
    QStringList bufStrings = getName().split("\\.");
    if (bufStrings.length() < 1) {
        return "";
    }
    return bufStrings.last();
}

QString ITMEvent::printExtraInfo()
{
    QString ret = "";
    if (isCyclesValueIsNotReal())
        ret = Messages::Event_NO_TIMESTAMP_RECEIVED_CYCLE_VALUE_GUESSED + ". ";
    if (localTimestamp != nullptr) {
        if ((localTimestamp->timeControl() & ITMLocalTimestampEvent::VALUE_Type::VALUE_DELAYED_REL_ASS_EV) > 0) {
            ret = ret + Messages::Event_TIMESTAMP_DELAYED + ". ";
        }
        if ((localTimestamp->timeControl() & ITMLocalTimestampEvent::VALUE_Type::VALUE_DELAYED_REL_ITM_DWT) > 0) {
            ret = ret + Messages::Event_PACKET_DELAYED + ". ";
        }
    }
    return ret;
}

QString ITMEvent::printTime()
{
    if (isCyclesValueIsNotReal()) {
        return "?";
    }

    double time = getTimes();

    QString prefix = "";
    if ((time < 1.0) && (time > 0.0)) {
        time *= 1000.0;
        prefix = "m";
        if ((time < 1.0) && (time > 0.0)) {
            time *= 1000.0;
            prefix = "µ";
            if ((time < 1.0) && (time > 0.0)) {
                time *= 1000.0;
                prefix = "n";
            }
        }
    }

    return QString("%1 %2").arg(time).arg(prefix);
}

QString ITMEvent::printCycles()
{
    return getCycles() + (isCyclesValueIsNotReal() ? " ?" : "");
}

QString ITMEvent::printData()
{
    return QString("");
}

// TODO implement operators rather
int ITMEvent::compareTo(ITMEvent *e)
{
    long ac = getCycles();
    long ec = e->getCycles();
    if (ac == ec)
        return 0;
    if (ac > ec) {
        return 1;
    }
    return -1;
}

QString ITMEvent::getName() const
{
    return m_name;
}
