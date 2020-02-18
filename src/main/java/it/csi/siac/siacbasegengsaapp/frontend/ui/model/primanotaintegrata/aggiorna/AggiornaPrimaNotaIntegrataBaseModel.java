/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegato;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
/**
 * Classe base di model per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
public abstract class AggiornaPrimaNotaIntegrataBaseModel extends BaseAggiornaPrimaNotaIntegrataBaseModel {

	/** Per la serializzazione	 */
	private static final long serialVersionUID = 3503574932819500780L;
	
	//GESTIONE DEI CONTI
	//fields per la gestione della tabella delle scritture
	private List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	
	//TODO: forse solo GSA
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private CausaleEP causaleEP;
	private RegistrazioneMovFin regMovFin;
	
	/**
	 * @return the descrizioneCausale
	 */
	public String getDescrizioneCausale() {
		CausaleEP cep = getCausaleEP();
		StringBuilder sb = new StringBuilder();
		if(cep != null) {
			sb.append(cep.getCodice())
				.append(" - ")
				.append(cep.getDescrizione());
		}
		return sb.toString();
	}
	
	/**
	 * @return the listaElementoScrittura
	 */
	public List<ElementoScritturaPrimaNotaIntegrata> getListaElementoScrittura() {
		return listaElementoScrittura;
	}

	/**
	 * @param listaElementoScrittura the listaElementoScrittura to set
	 */
	public void setListaElementoScrittura(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura) {
		this.listaElementoScrittura = listaElementoScrittura;
	}
	

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}
	
	
	/**
	 * @return the regMovFin
	 */
	public RegistrazioneMovFin getRegMovFin() {
		return regMovFin;
	}

	/**
	 * @param regMovFin the regMovFin to set
	 */
	public void setRegMovFin(RegistrazioneMovFin regMovFin) {
		this.regMovFin = regMovFin;
	}

	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}
	
	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}

	/* **** Requests **** */

	/**
	 * Ottiene la lista dei movimenti EP con l'ambito correttamente popolato.
	 * 
	 * @return la lista dei movimenti
	 */
	@Override
	public List<MovimentoEP> ottieniListaMovimentiEPConAmbito() {
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = new ArrayList<MovimentoDettaglio>();
		//passo dall'utility all'oggetto List<MovimentiEP>
		int numeroRiga = 0;
		for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : getListaElementoScritturaPerElaborazione()){
			if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null && elementoScrittura.getMovimentoDettaglio().getImporto()!= null){
				MovimentoDettaglio movimentoDettaglio = elementoScrittura.getMovimentoDettaglio();
				movimentoDettaglio.setNumeroRiga(numeroRiga);
				listaMovimentiDettaglioFinal.add(movimentoDettaglio);
				numeroRiga++;
			}
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(getCausaleEP());
		movEP.setRegistrazioneMovFin(getRegMovFin());
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setAmbito(getAmbito());
		
		return Arrays.asList(movEP);
	}
	
	
	/**
	 * @return the azioneAgggiornamento
	 */
	@Override
	public String getAzioneAggiornamento(){
		return "aggiornaPrimaNotaIntegrata" + getAmbito().getSuffix() + "_aggiorna";
	}
	
	/**
	 * Crea una request per il servizio di {@link CalcolaImportoMovimentoCollegato}.
	 * @return la request creata
	 */
	public CalcolaImportoMovimentoCollegato creaRequestCalcolaImportoMovimentoCollegato() {
		CalcolaImportoMovimentoCollegato req = creaRequest(CalcolaImportoMovimentoCollegato.class);
		req.setRegistrazioneMovFin(getRegMovFin());
		return req;
	}

}
