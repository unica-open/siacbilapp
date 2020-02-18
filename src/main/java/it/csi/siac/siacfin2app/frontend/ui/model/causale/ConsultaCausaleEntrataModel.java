/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale.ElementoCausale;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale.ElementoCausaleEntrataFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;

/**
 * Classe di model per la consultazione del Causale di Entrata
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 *
 */
public class ConsultaCausaleEntrataModel extends GenericCausaleEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5481575444709997686L;
	
	private Integer uidDaConsultare;
	private List<ElementoCausale> listaStoricoCausale = new ArrayList<ElementoCausale>();
			
	/** Costruttore vuoto di default */
	public ConsultaCausaleEntrataModel() {
		setTitolo("Consultazione Causale di Incasso");
	}
	
	/**
	 * @return the uidDaConsultare
	 */
	public Integer getUidDaConsultare() {
		return uidDaConsultare;
	}
	
	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(Integer uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}


	/**
	 * @return the listaStoricoCausale
	 */
	public List<ElementoCausale> getListaStoricoCausale() {
		return listaStoricoCausale;
	}
	

	/**
	 * @param listaStoricoCausale the listaStoricoCausale to set
	 */
	public void setListaStoricoCausale(List<ElementoCausale> listaStoricoCausale) {
		this.listaStoricoCausale = listaStoricoCausale;
	}
	
	
	/** 
	 * Setter personalizzato (converte la lista della response nel wrapper utilizzato dal front-end)
	 * 
	 * @param listaStoricoCausale                   the listaStoricoCausale to wrap
	 * @param listaTipoAtto                         la lista dei tipi di atto
	 * @param listaStrutturaAmministrativoContabile la lista delle Strutture
	 */
	public void setListaStoricoCausale(List<CausaleEntrata> listaStoricoCausale, List<TipoAtto> listaTipoAtto,
			List<StrutturaAmministrativoContabile>listaStrutturaAmministrativoContabile) {
		this.listaStoricoCausale = ElementoCausaleEntrataFactory.getInstances(listaStoricoCausale, listaTipoAtto,
				listaStrutturaAmministrativoContabile, isGestioneUEB());
	}
	
	/* ***** Requests ***** */
	
	/**
	 * Crea una request per il servizio di {@link DettaglioStoricoCausaleEntrata}.
	 * 
	 * @return la request creata
	 */
	public DettaglioStoricoCausaleEntrata creaRequestDettaglioStoricoCausaleEntrata() {
		DettaglioStoricoCausaleEntrata request = new DettaglioStoricoCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleEntrata causale = new CausaleEntrata();
		causale.setUid(getUidDaConsultare());
		request.setCausale(causale);
		
		return request;
	}
	
}
