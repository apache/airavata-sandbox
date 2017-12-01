from django.views import generic
from django.views.generic import View
from django.views.generic.edit import CreateView, UpdateView, DeleteView
from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login
from django.core.urlresolvers import reverse_lazy
from .models import Request
from .forms import UserForm


class IndexView(generic.ListView):
    template_name = 'dashboard/index.html'
    context_object_name = 'all_requests'

    def get_queryset(self):
        return Request.objects.all()


class DetailView(generic.DetailView):
    model = Request
    template_name = 'dashboard/detail.html'


class RequestCreate(CreateView):
    model = Request
    fields = ['request_title', 'request_status', 'cpu_hours_requested', 'cpu_hours_allocated']


class RequestUpdate(UpdateView):
    model = Request
    fields = ['request_title', 'request_status', 'cpu_hours_requested', 'cpu_hours_allocated']


class RequestDelete(DeleteView):
    model = Request
    success_url = reverse_lazy('dashboard:index')


class UserFormView(View):
    form_class = UserForm
    template_name = 'dashboard/registration_form.html'


    # Display a blank form
    def get(self, request):
        form = self.form_class(None)
        return render(request, self.template_name, {'form': form})

    # process form data
    def post(self, request):
        form = self.form_class(request.POST)

        if form.is_valid():
            user = form.save(commit=False)

            # cleaned (normalized) data
            username = form.cleaned_data['username']
            password = form.cleaned_data['password']
            user.set_password(password)
            user.save()

            # returns user objects if credentials are correct
            user = authenticate(username=username, password=password)
            if user is not None:
                if user.is_active:
                    login(request, user)
                    return redirect('dashboard:index')
        return render(request, self.template_name, {'form': form})





# # from django.http import Http404
# # from django.http import HttpResponse
# # from django.template import loader
# from django.shortcuts import render, get_object_or_404
# from .models import Album, Song
#
# def index(request):
#     all_albums = Album.objects.all()
#     context = {'all_albums': all_albums}
#     return render(request, 'dashboard/index.html', context)
#
#
#
#     # html = ''
#     # all_albums = Album.objects.all()
#     # for album in all_albums:
#     #     url = '/dashboard/' + str(album.id) + '/'
#     #     html += '<a href="' + url + '">' + album.album_title + '</a><br>'
#     # return HttpResponse(html)
#
# def detail(request, album_id):
#     album = get_object_or_404(Album, pk=album_id)
#     # try:
#     #     album = Album.objects.get(pk=album_id)
#     # except Album.DoesNotExist:
#     #     raise Http404("Album does not exists")
#     return render(request, 'dashboard/detail.html', {'album':album} )
#     # return HttpResponse("<h2>This will contain detaails for " +str(album_id) + "</h2>")
#
#
# def favourite(request, album_id):
#     album = get_object_or_404(Album, pk=album_id)
#     try:
#         selected_song = album.song_set.get(pk=request.POST['song'])
#     except (KeyError, Song.DoesNotExist):
#         return render(request, 'dashboard/detail.html', {
#             'album' : album,
#             'error_message' : "You did not select a valid song",
#         })
#     else:
#         selected_song.is_favourite = True
#         selected_song.save()
#         return render(request, 'dashboard/detail.html', {'album': album})

#
#
#
#
