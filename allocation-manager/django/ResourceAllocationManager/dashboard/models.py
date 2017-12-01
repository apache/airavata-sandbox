from __future__ import unicode_literals

from django.db import models
from django.core.urlresolvers import reverse



# Create your models here.
class Request(models.Model):
    request_title = models.CharField(max_length=250)
    request_description = models.CharField(max_length=500)
    request_status = models.CharField(max_length=20)
    request_comments = models.FileField()
    allocation_type = models.CharField(max_length=100)
    cpu_hours_requested = models.CharField(max_length=10)
    cpu_hours_allocated = models.CharField(max_length=10)


    def get_absolute_url(self):
        return reverse('dashboard/detail.html', kwargs={'pk': self.pk})


    def __str__(self):
        return self.request_title + ' - ' + self.request_status


