namespace org.apache.airavata.allocation.manager.admin;
      
      
    service ManageReview {  
            void addReviewers(1:string projectId, 2: required list<string> reviewers), 

            void removeReviewer(1:string projectId, 2:string reviewer)

    }