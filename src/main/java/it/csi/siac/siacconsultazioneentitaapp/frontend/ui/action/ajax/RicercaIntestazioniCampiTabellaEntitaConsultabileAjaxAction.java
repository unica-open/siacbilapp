/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.RicercaIntestazioniCampiTabellaEntitaConsultabileAjaxModel;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.DataAdapterFactory;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.EntitaConsultabileDataAdapter;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileDataWrapper;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per ottenere l'elenco di intestazioni della tabella a partire dal tipo di entita che si intende consultare
 * @author Elisa Chiari
 * @version 1.0.0 - 24/03/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaIntestazioniCampiTabellaEntitaConsultabileAjaxAction extends GenericBilancioAction<RicercaIntestazioniCampiTabellaEntitaConsultabileAjaxModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -1671030035630856273L;
	
	@Autowired
	private transient DataAdapterFactory dataAdapterFactory;
	
	@Override
	public String execute(){
		List<EntitaConsultabileDataWrapper> listaIntestazioniCampiTabella = getElencoIntestazioniColonne(model.getTipoEntitaConsultabile());
		if( listaIntestazioniCampiTabella.isEmpty()){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
		model.setListaIntestazioniCampiTabella(listaIntestazioniCampiTabella);
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene l'elenco di intestazioni delle colonne dell'entita selezionabile
	 * @param tipoEntitaConsultabile l'entita di cuoi voglio ottenere le intestazioni della tabella
	 * @return the list of intestazioni delle colonne delle tabelle
	 */
	public List<EntitaConsultabileDataWrapper> getElencoIntestazioniColonne(TipoEntitaConsultabile tipoEntitaConsultabile){
		final String methodName = "getElencoIntestazioniColonne";
		if(tipoEntitaConsultabile == null){
			log.debug(methodName, "TipoEntitaConsultabile non presente");
			return new ArrayList<EntitaConsultabileDataWrapper>();	
		}
		EntitaConsultabileDataAdapter columnAdapter = dataAdapterFactory.init(tipoEntitaConsultabile);
		return columnAdapter.getListaIntestazioneColonneTabella();
	}
}
