from __future__ import unicode_literals

from django.db import models
from django.core.urlresolvers import reverse
#from datetime import date

# Create your models here.
class Request(models.Model):
    request_title = models.CharField(max_length=250)
    request_description = models.CharField(max_length=500)
    request_status = models.CharField(max_length=20)
    allocation_type = models.CharField(max_length=100)
    applications_to_be_used = models.CharField(max_length=100)
    disk_usage_range_per_job = models.BigIntegerField()
    document = models.FileField()
    field_of_science = models.CharField(max_length=50)
    keywords = models.CharField(max_length=100)
    max_memory_per_cpu = models.BigIntegerField()
    num_cpus_per_job = models.BigIntegerField()
    request_reviewed_and_funded_by = models.CharField(max_length=100)
    request_date = models.DateTimeField(blank=True, null=True)
    service_units = models.BigIntegerField()
    specific_resource_selection = models.CharField(max_length=100)
    typical_su_per_job = models.BigIntegerField()

    def get_absolute_url(self):
        return reverse('dashboard/detail.html', kwargs={'pk': self.pk})


    def __str__(self):
        return self.request_title + ' - ' + self.request_status


