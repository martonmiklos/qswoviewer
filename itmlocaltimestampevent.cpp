#include "itmlocaltimestampevent.h"

#include "timestampevent.h"

ITMLocalTimestampEvent::ITMLocalTimestampEvent(quint8 timeControl, long timestamp) :
    TimestampEvent(timestamp),
    m_timeControl(timeControl)
{

}

quint8 ITMLocalTimestampEvent::timeControl() const
{
    return m_timeControl;
}
