/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.tipoonere;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnere;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;


/**
 * Classe di model per l'inserimento del Tipo Onere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2014
 *
 */
public class InserisciTipoOnereModel extends GenericTipoOnereModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 285759010049596864L;
	
	private List<AttivitaOnere> attivitaOnere = new ArrayList<AttivitaOnere>();
	private List<Causale770> causali770 = new ArrayList<Causale770>();
	private List<CodiceSommaNonSoggetta> sommeNonSoggette =  new ArrayList<CodiceSommaNonSoggetta>();
	
	/** Costruttore vuoto di default */
	public InserisciTipoOnereModel() {
		setTitolo("Inserimento onere");
	}

	/**
	 * @return the attivitaOnere
	 */
	public List<AttivitaOnere> getAttivitaOnere() {
		return attivitaOnere;
	}

	/**
	 * @param attivitaOnere the attivitaOnere to set
	 */
	public void setAttivitaOnere(List<AttivitaOnere> attivitaOnere) {
		this.attivitaOnere = attivitaOnere != null ? attivitaOnere : new ArrayList<AttivitaOnere>();
	}

	/**
	 * @return the causali770
	 */
	public List<Causale770> getCausali770() {
		return causali770;
	}

	/**
	 * @param causali770 the causali770 to set
	 */
	public void setCausali770(List<Causale770> causali770) {
		this.causali770 = causali770 != null ? causali770 : new ArrayList<Causale770>();
	}
	/**
	 * @return the sommeNonSoggette
	 */
	public List<CodiceSommaNonSoggetta> getSommeNonSoggette() {
		return sommeNonSoggette;
	}

	/**
	 * @param sommeNonSoggette the sommeNonSoggette to set
	 */
	public void setSommeNonSoggette(List<CodiceSommaNonSoggetta> sommeNonSoggette) {
		this.sommeNonSoggette = sommeNonSoggette;
	}
//	
//	/**
//	 * Setter di wrap per le somme non soggette
//	 * @return the codiciSommeNonSoggette
//	 */
//	public List<String> getCodiciSommeNonSoggette() {
//		List<String> result = new ArrayList<String>();
//		for(TipoSommaNonSoggetta tsns : this.sommeNonSoggette){
//			result.add(tsns.getCodice());
//		}
//		return result;
//	}
//
//	/**
//	 * Getter di wrap per le somme non soggette
//	 * @param codiciSommeNonSoggette the codiciSommeNonSoggette to set
//	 */
//	public void setCodiciSommeNonSoggette(List<String> codiciSommeNonSoggette) {
//		List<TipoSommaNonSoggetta> list = new ArrayList<TipoSommaNonSoggetta>(); 
//		for (String codice : codiciSommeNonSoggette) {
//			TipoSommaNonSoggetta tsns = TipoSommaNonSoggetta.byCodice(codice);
//			list.add(tsns);
//		}
//		this.sommeNonSoggette = list;
//	}
	
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceTipoOnere}.
	 * 
	 * @return la request creata
	 */
	public InserisceTipoOnere creaRequestInserisceTipoOnere() {
		InserisceTipoOnere request = creaRequest(InserisceTipoOnere.class);
		
		// Popolo il TipoOnere
		// Aggiungo le causali 770 (Problema di Struts2? Non riesco a farle injettare direttamente nemmeno con un setter specifico)
		getTipoOnere().getCausali().addAll(getCausali770());
		getTipoOnere().setCodiciSommaNonSoggetta(getSommeNonSoggette());
		// Injetto le attivita' per simmetria con le causali
		getTipoOnere().setAttivitaOnere(getAttivitaOnere());
		request.setTipoOnere(getTipoOnere());
		
		return request;
	}
	
}
