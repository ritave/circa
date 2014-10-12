from django.http.response import HttpResponse
from django.shortcuts import render
from csa.models import *
from django.views.decorators.csrf import csrf_exempt
from rest_framework import viewsets
from rest_framework.permissions import AllowAny
from csa.serializers import *
from rest_framework import generics


class NotificationList(generics.ListCreateAPIView):
    """
    List all notifications (GET) or create new (POST)
    """
    serializer_class = NotificationSerializer
    permission_classes = (AllowAny,)
    paginate_by = None
    model = Notification


class NotificationInArea(generics.ListAPIView):
    """
    List only the notifications near the client
    """
    serializer_class = NotificationSerializer
    permission_classes = (AllowAny,)
    paginate_by = None

    def get_queryset(self):
        latitude = float(self.kwargs['latitude'])
        longitude = float(self.kwargs['longitude'])
        queryset = Notification.getObjectsInRange(latitude,longitude)
        return queryset

# upvote a notification
@csrf_exempt
def vote_positive(request, id):
    if request.method == "POST":
        Notification.objects.get(id=id).addPositiveVote()
        return HttpResponse("ok")
    return HttpResponse("invalid method")

# downvote a notification
@csrf_exempt
def vote_negative(request, id):
    if request.method == "POST":
        Notification.objects.get(id=id).addNegativeVote()
        rating = Notification.objects.get(id=id).confirmed - Notification.objects.get(id=id).debunk
        if (rating < -1):
            Notification.objects.get(id=id).delete()
        return HttpResponse("ok")
    return HttpResponse("invalid method")