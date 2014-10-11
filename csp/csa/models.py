from django.db import models

class Notification(models.Model):
    FREEWATER = 0
    PICTURESPOT = 1
    DANGER = 2
    FREEWIFI = 3
    OTHER = 4

    CHECKIN_CHOICES = (
        (FREEWATER, 'Free Water'),
        (PICTURESPOT, 'Picture Spot'),
        (DANGER, 'Danger'),
        (FREEWIFI, 'Free WiFi'),
        (OTHER, 'Other')
    )

    created_at = models.DateTimeField(auto_now_add=True)
    latitude = models.FloatField() #Central Hotel
    longitude = models.FloatField()
    kind = models.PositiveSmallIntegerField(max_length=1, choices=CHECKIN_CHOICES, default=FREEWIFI)
    text_note = models.TextField(max_length=160, null=True, blank=True)
    confirmed = models.PositiveIntegerField(default=1)
    debunk = models.PositiveIntegerField(default=0)

    def getObjectsInRange(latitude, longitude):
        RADIUS = 0.1

        closeObjects = Notification.objects.filter(latitude__lte=latitude+RADIUS).\
            filter(latitude__gte=latitude-RADIUS).\
            filter(longitude__lte=longitude+RADIUS).\
            filter(longitude__gte=longitude-RADIUS)

        return closeObjects

    def addPositiveVote(self):
        self.positive += 1
        self.save()

    def addNegativeVote(self):
        self.negative += 1
        self.save()
        # Add parameter for deleting cell