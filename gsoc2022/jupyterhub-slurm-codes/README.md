# This folder contains the codes for the jupyterhub run.

- For the local setup using Dockerspawner, you would want to run the local jupyterhub installation with the dockerspawner_config.py file.
Replace the c.DockerSpawner.host_homedir_format_string with your appropriate home directory for jupyterhub.

- jupyterhub_config_final.py contains the final version of the config file with the containerization logic for jupyterhub process running on the spawned server.
With jupyterhub installed and the slurm cluster setup, run jupyterhub with this config file, to see the output.
You can add the ip of the spawned server from /var/log/slurm/slurm_elastic.log to /etc/hosts file.

- dockerfile contains the commands used to build the used docker image on the server with jupyterhub and batchspawner installed.
To view the image on dockerhub, go to himanshuhansaria/jhub-gsoc2:latest for the latest docker image used in the project.
