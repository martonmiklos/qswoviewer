#ifndef ITMGLOBALTIMESTAMPEVENT_H
#define ITMGLOBALTIMESTAMPEVENT_H

#include "timestampevent.h"

class ITMGlobalTimestampEvent : public TimestampEvent
{
public:
    ITMGlobalTimestampEvent(long timestamp, byte clkch, byte wrap, byte packageFormat);
};

#endif // ITMGLOBALTIMESTAMPEVENT_H
