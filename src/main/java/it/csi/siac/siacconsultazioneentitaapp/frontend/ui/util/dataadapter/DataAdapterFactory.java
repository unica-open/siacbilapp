/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Factory per i dataAdapter.
 * 
 * @author Domenico Lisi
 * @see EntitaConsultabileDataAdapter
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DataAdapterFactory {
	
	/**
	 * Inizializza il DataAdapter per il tipo entita consultabile passato come parametro.
	 * 
	 * @param tipoEntitaConsultabile il tipo di entita
	 * @return dataAdapter
	 */
	public EntitaConsultabileDataAdapter init(TipoEntitaConsultabile tipoEntitaConsultabile) {
		
		/* TODO Per ora l'implementazione e' statica e basta sull'enum EntitaConsultabiliAdapters.
		 * Una seconda versione istanziera' gli adapter a partire da configurazioni reperite da un servizio ad hoc.
		 * In questo modo ogni ente potra' personalizzare la propria configurazione.
		 * Tali configurazioni posson essere mantenuti in una cache locale (differenziata per ente).
		 */
		
		//V1
		EntitaConsultabiliAdapters ec = EntitaConsultabiliAdapters.byTipoEntitaConsultabile(tipoEntitaConsultabile);
		return ec.newEntitaConsultabileDataAdapterInstance();
		
		//V2
		//res = consultazioneEntitaService.ottieniDataAdapter(tipoEntitaConsultabile);
		//return new EntitaConsultabileDataAdapter(res.getColumns(), res.getColumnsExport(), res.getDatas());
		
		
		/*
		 * Prevedere tabella su DB con:
		 *  ______________________________________________________________________________________________________________________________________
		 * | TipoEntitaConsultabile | Name (nome della colonna) | Pattern | CampiKeys (chiavi dei Campi separati da virgola) | Ordinamento | Ente |
		 * |―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――|――――――|
		 * | CAPITOLOSPESA          | Strutt. Amm. Resp.        |{0} - {1}| classif_sac_code, classif_sac_desc               | 2           | 1    |
		 * |―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――|――――――|
		 * | CAPITOLOSPESA          | P.d.c. finanziario        |{0}      | classif_pdc_code                                 | 1           | 1    |
		 * |―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――|――――――
		 */
	}
}
