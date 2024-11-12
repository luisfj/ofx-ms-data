create unique index if not exists unk_lancamentos_importados 
on operacao (fit_id, data_hora, id_ue, valor) 
where id_importacao is not null;