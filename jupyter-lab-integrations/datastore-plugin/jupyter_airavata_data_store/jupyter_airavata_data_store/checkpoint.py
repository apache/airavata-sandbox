from jupyter_airavata_data_store.ipycompact import Checkpoints, GenericCheckpointsMixin

class AiravataCheckpoints(GenericCheckpointsMixin, Checkpoints):

    def create_notebook_checkpoint(self, nb, path):
        """Create a checkpoint of the current state of a notebook
        Returns a checkpoint_id for the new checkpoint.
        """
        self.log.info("Airavata.checkpounts.create_notebook_checkpoint: ('%s')", path)

    def create_file_checkpoint(self, content, format, path):
        """Create a checkpoint of the current state of a file
        Returns a checkpoint_id for the new checkpoint.
        """
        self.log.info("Airavata.checkpounts.create_file_checkpoint: ('%s')", path)

    def delete_checkpoint(self, checkpoint_id, path):
        """delete a checkpoint for a file"""
        self.log.info("Airavata.checkpounts.delete_checkpoint: ('%s') ('%s')", checkpoint_id, path)

    def get_checkpoint_content(self, checkpoint_id, path):
        """Get the content of a checkpoint."""
        self.log.info("Airavata.checkpounts.get_checkpoint_content: ('%s') ('%s')", checkpoint_id, path)

    def get_notebook_checkpoint(self, checkpoint_id, path):
        self.log.info("Airavata.checkpounts.get_notebook_checkpoint: ('%s') ('%s')", checkpoint_id, path)

    def get_file_checkpoint(self, checkpoint_id, path):
        self.log.info("Airavata.checkpounts.get_file_checkpoint: ('%s') ('%s')", checkpoint_id, path)

    def list_checkpoints(self, path):
        """Return a list of checkpoints for a given file"""
        self.log.info("Airavata.checkpounts.list_checkpoints: ('%s')", path)

    def rename_all_checkpoints(self, old_path, new_path):
        """Rename all checkpoints for old_path to new_path."""
        self.log.info("Airavata.checkpounts.rename_all_checkpoints: ('%s') ('%s')", old_path, new_path)

    def delete_all_checkpoints(self, path):
        """Delete all checkpoints for the given path."""
        self.log.info("Airavata.checkpounts.delete_all_checkpoints: ('%s')", path)



