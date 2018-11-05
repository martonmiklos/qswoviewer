#ifndef ITMLOCALTIMESTAMPEVENT_H
#define ITMLOCALTIMESTAMPEVENT_H

#include <QtGlobal>

#include "timestampevent.h"

class TimestampEvent;

class ITMLocalTimestampEvent : public TimestampEvent
{
public:
    ITMLocalTimestampEvent(quint8 timeControl, long int timeStamp);

    quint8 TYPE_MASK = 3;
    enum VALUE_Type {
        VALUE_SYNC = 0,
        VALUE_DELAYED_REL_ITM_DWT = 1,
        VALUE_DELAYED_REL_ASS_EV = 2,
        VALUE_DELAYED_REL_ASS_EV_ITM_DWT = 3
    };

    quint8 timeControl() const;

private:
    quint8 m_timeControl;
    long m_timeStamp;
};

#endif // ITMLOCALTIMESTAMPEVENT_H
