#ifndef DWTEXCEPTIONEVENT_H
#define DWTEXCEPTIONEVENT_H

#include "itmevent.h"

class DWTExceptionEvent : public ITMEvent
{
public:
    DWTExceptionEvent(int exceptionNumber, quint8 function);
};

#endif // DWTEXCEPTIONEVENT_H
