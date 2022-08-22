SET sql_safe_updates=0;
update OEstorage.molecule set jour_cit = replace(jour_cit,'\"','\'') where  jour_cit like "%\"%";
update OEstorage.molecule set inter_thngs = replace(inter_thngs,'\"','\'') where  inter_thngs like "%\"%";
update OEstorage.molecule set comment_mol = replace(comment_mol,'\"','\'') where  comment_mol like "%\"%";
commit;