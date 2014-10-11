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

    unique_id = models.BigIntegerField(primary_key=True)
    date = models.DateTimeField(auto_now_add=True)
    latitude = models.FloatField()
    longitude = models.FloatField()
    checkin_type = models.PositiveSmallIntegerField(max_length=1, choices=CHECKIN_CHOICES)
    other_description = models.TextField(max_length=160, null=True, blank=True)
    positive = models.PositiveIntegerField()
    negative = models.PositiveIntegerField()

    def getObjectsInRange(longitude, latitude):
        RADIUS = 0.001
        return Notification.objects.all()