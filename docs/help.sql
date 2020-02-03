
## query for all lines
	select * from ks_odtobjectdataline where ks_odtobjectdata_id in
	(
		select ks_odtobjectdata_id from ks_odtobjectdata where ks_odtversion_id in
		(
			select ks_odtversion_id from ks_odtversion where ks_odtpackage_id in
			(
				select ks_odtpackage_id from ks_odtpackage where name = 'kbs.plugins.trekglobal.rest'
			)
		)
	)
	
## delete whole package
	-- lines
	delete from ks_odtobjectdataline where ks_odtobjectdata_id in
	(
		select ks_odtobjectdata_id  from ks_odtobjectdata where ks_odtversion_id in
		(
			select ks_odtversion_id  from ks_odtversion where ks_odtpackage_id in
			(
				select ks_odtpackage_id  from ks_odtpackage where name = 'kbs.plugins.trekglobal.rest'
			)
		)
	);
	-- data
	delete from ks_odtobjectdata where ks_odtversion_id in
	(
		select ks_odtversion_id  from ks_odtversion where ks_odtpackage_id in
		(
			select ks_odtpackage_id  from ks_odtpackage where name = 'kbs.plugins.trekglobal.rest'
		)
	);
	-- version
	delete from ks_odtversion where ks_odtpackage_id in
	(
		select ks_odtpackage_id  from ks_odtpackage where name = 'kbs.plugins.trekglobal.rest'
	);
	-- package
	delete from ks_odtpackage where name = 'kbs.plugins.trekglobal.rest';