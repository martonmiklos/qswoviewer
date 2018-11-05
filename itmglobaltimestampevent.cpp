#include "itmglobaltimestampevent.h"

ITMGlobalTimestampEvent::ITMGlobalTimestampEvent(long timestamp, quint8 clkch, quint8 wrap, quint8 packageFormat) :
    TimestampEvent(timestamp),
    m_clkch(clkch),
    m_wrap(wrap),
    m_packageFormat(packageFormat)
{

}
