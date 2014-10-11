from django.db import models

class Notification(models.Model):
    FREEWATER = 1
    PICTURESPOT = 2
    DANGER = 3
    FREEWIFI = 4
    OTHER = 5

    CHECKIN_CHOICES = (
        (FREEWATER, 'Free Water'),
        (PICTURESPOT, 'Picture Spot'),
        (DANGER, 'Danger'),
        (FREEWIFI, 'Free WiFi'),
        (OTHER, 'Other')
    )

    date = models.DateTimeField(auto_now_add=True)
    latitude = models.FloatField() #Central Hotel
    longitude = models.FloatField()
    checkin_type = models.PositiveSmallIntegerField(max_length=1, choices=CHECKIN_CHOICES, default=FREEWIFI)
    other_description = models.TextField(max_length=160, null=True, blank=True)
    positive = models.PositiveIntegerField(default=1)
    negative = models.PositiveIntegerField(default=0)

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