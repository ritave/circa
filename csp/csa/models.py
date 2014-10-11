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

    unique_id = models.BigIntegerField(primary_key=True, default=42)
    date = models.DateTimeField(auto_now_add=True)
    latitude = models.FloatField(default=47.377105) #Central Hotel
    longitude = models.FloatField(default=8.543686)
    checkin_type = models.PositiveSmallIntegerField(max_length=1, choices=CHECKIN_CHOICES, default=FREEWIFI)
    other_description = models.TextField(max_length=160, null=True, blank=True, default=null)
    positive = models.PositiveIntegerField(default=1)
    negative = models.PositiveIntegerField(default=0)

    def getObjectsInRange(latitude, longitude):
        RADIUS = 0.1

        closeObjects = Notification.objects.filter(latitude__leq=latitude+RADIUS).filter(latitude__geq=latitude-RADIUS).filter(longitude__leq=longitude+RADIUS).filter(longitude__geq=longitude-RADIUS)

        return closeObjects

    def addPositiveVote(self):
        self.positive += 1

    def addNegativeVote(self):
        self.negative += 1
        # Add parameter for deleting cell