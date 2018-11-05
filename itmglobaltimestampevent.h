#ifndef ITMGLOBALTIMESTAMPEVENT_H
#define ITMGLOBALTIMESTAMPEVENT_H

#include "timestampevent.h"

class ITMGlobalTimestampEvent : public TimestampEvent
{
public:
    ITMGlobalTimestampEvent(long timestamp, quint8 clkch, quint8 wrap, quint8 packageFormat);
private:
    quint8 m_clkch;
    quint8 m_wrap;
    quint8 m_packageFormat;
};

#endif // ITMGLOBALTIMESTAMPEVENT_H
