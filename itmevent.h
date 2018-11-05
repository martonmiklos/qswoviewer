#ifndef ITMEVENT_H
#define ITMEVENT_H

#include <QString>
#include <QList>

#include "messages.h"

class ITMLocalTimestampEvent;

class ITMEvent
{
public:
    ITMEvent();
    QString getEventsText(QList<ITMEvent*> eventList);

    bool isCyclesValueIsNotReal();
    void setCyclesValueIsNotReal(bool cyclesValueIsNotReal);

    long getCycles();
    void setCycles(long cycles);

    double getTimes();
    void setTimes(double times);

    long getEventID();
    void setEventID(long eventID);

    void setEventType(quint8 eventType);
    quint8  getEventType();

    long getPcTimestamp();
    void setPcTimestamp(long pcTimestamp);

    ITMLocalTimestampEvent* getLocalTimestamp();
    void setLocalTimestamp(ITMLocalTimestampEvent *timestampEvent);

    QString printType();
    QString printExtraInfo();
    QString printTime();
    QString printCycles();
    QString printData();

    int compareTo(ITMEvent *e);

    QString getName() const;

private:
    QString m_name;
    QString EMPTY_STRING = "";
    QString PIPE = " | ";
    quint8 eventType;
    long cycles = 0L;
    double times = 0.0;
    long eventID = 0L;
    quint64 pcTimestamp = 0;
    bool cyclesValueIsNotReal;
    ITMLocalTimestampEvent *localTimestamp = nullptr;

};

#endif // ITMEVENT_H
