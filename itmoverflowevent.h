#ifndef ITMOVERFLOWEVENT_H
#define ITMOVERFLOWEVENT_H

#include "itmevent.h"

class ITMOverflowEvent : public ITMEvent
{
public:
    ITMOverflowEvent(bool generatedBySync);
private:
    bool m_generatedBySync;
};

#endif // ITMOVERFLOWEVENT_H
