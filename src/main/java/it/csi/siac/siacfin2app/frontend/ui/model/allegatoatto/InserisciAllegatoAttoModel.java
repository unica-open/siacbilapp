/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

/**
 * Classe di model per l'inserimento dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.1 - 11/set/2014
 *
 */
public class InserisciAllegatoAttoModel extends GenericAllegatoAttoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3509011605563225740L;

	private static final String AZIONE_DECENTRATA = "OP-COM-insAttoAllegatoDec";
	
	private TipoAtto tipoAttoALG;
	private String messaggioAggiuntivo = "";
	
	private Boolean attoAutomatico;
	
	private List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	
	/** Costruttore vuoto di default */
	public InserisciAllegatoAttoModel() {
		super();
		setTitolo("Inserisci allegato atto");
	}

	/**
	 * @return the listaElencoDocumentiAllegato
	 */
	public List<ElencoDocumentiAllegato> getListaElencoDocumentiAllegato() {
		return listaElencoDocumentiAllegato;
	}

	/**
	 * @param listaElencoDocumentiAllegato the listaElencoDocumentiAllegato to set
	 */
	public void setListaElencoDocumentiAllegato(List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato) {
		this.listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(listaElencoDocumentiAllegato, ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}
	
	/**
	 * @return the tipoAttoALG
	 */
	public TipoAtto getTipoAttoALG() {
		return tipoAttoALG;
	}
	/**
	 * @param tipoAttoALG the tipoAttoALG to set
	 */
	public void setTipoAttoALG(TipoAtto tipoAttoALG) {
		this.tipoAttoALG = tipoAttoALG;
	}
	/**
	 * @return the messaggioAggiuntivo
	 */
	public String getMessaggioAggiuntivo() {
		return messaggioAggiuntivo;
	}

	/**
	 * @param messaggioAggiuntivo the messaggioAggiuntivo to set
	 */
	public void setMessaggioAggiuntivo(String messaggioAggiuntivo) {
		this.messaggioAggiuntivo = messaggioAggiuntivo;
	}
	
	/**
	 * @return the attoAutomatico
	 */
	public Boolean getAttoAutomatico() {
		return attoAutomatico;
	}

	/**
	 * @param attoAutomatico the attoAutomatico to set
	 */
	public void setAttoAutomatico(Boolean attoAutomatico) {
		this.attoAutomatico = attoAutomatico;
	}

	/**
	 * @return the nomeAzioneDecentrata
	 */
	public String getNomeAzioneDecentrata() {
		return AZIONE_DECENTRATA;
	}
	
	@Override
	public String getDenominazioneAllegatoAtto() {
		StringBuilder sb = new StringBuilder();
		sb.append(getAttoAmministrativo().getAnno())
			.append("/")
			.append(getAttoAmministrativo().getNumero())
			.append("/")
			.append(getAttoAmministrativo().getTipoAtto().getCodice());
		if(getAttoAmministrativo().getStrutturaAmmContabile() != null) {
			sb.append("/")
				.append(getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
		}
		return sb.toString();
	}
	
	/**
	 * @return the totaleEntrataListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleEntrataListaElencoDocumentiAllegato() {
		BigDecimal result = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato eda : listaElencoDocumentiAllegato) {
			result = result.add(eda.getTotaleQuoteEntrate());
		}
		return result;
	}
	
	/**
	 * @return the totaleSpesaListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleSpesaListaElencoDocumentiAllegato() {
		BigDecimal result = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato eda : listaElencoDocumentiAllegato) {
			result = result.add(eda.getTotaleQuoteSpese());
		}
		return result;
	}
	
	/**
	 * @return the totaleNettoListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleNettoListaElencoDocumentiAllegato() {
		// Netto = entrata - spesa
		return getTotaleEntrataListaElencoDocumentiAllegato().subtract(getTotaleSpesaListaElencoDocumentiAllegato());
	}

	/* **** Requests **** */
	/**
	 * Crea una request per il servizio di {@link InserisceAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public InserisceAllegatoAtto creaRequestInserisceAllegatoAtto() {
		InserisceAllegatoAtto request = creaRequest(InserisceAllegatoAtto.class);
		
		// Imposto l'atto amministrativo nell'allegato atto
		getAllegatoAtto().setAttoAmministrativo(getAttoAmministrativo());
		getAllegatoAtto().setEnte(getEnte());
		// Lo stato operativo quale sarà?
		getAllegatoAtto().setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
		
		request.setAllegatoAtto(getAllegatoAtto());
		request.setBilancio(getBilancio());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}. Controlla se il provvedimento sia legato a una quota o a un elenco.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimentoCollegato() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setUid(getAttoAmministrativo().getUid());
		ricercaAtti.setConDocumentoAssociato(true);
		
		request.setRicercaAtti(ricercaAtti);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AssociaElenco}.
	 * 
	 * @return la request creata
	 */
	public AssociaElenco creaRequestAssociaElenco() {
		AssociaElenco request = creaRequest(AssociaElenco.class);
		request.setBilancio(getBilancio());
		
		// Minimizzo i dati dell'allegato
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		getElencoDocumentiAllegato().setAllegatoAtto(aa);
		
		getElencoDocumentiAllegato().setEnte(getEnte());
		
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		return request;
	}

	
	/**
	 * Crea una request per il servizio di {@link InserisceProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public InserisceProvvedimento creaRequestInserisceProvvedimento() {
		InserisceProvvedimento request = creaRequest(InserisceProvvedimento.class);
		
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setAnno(getBilancio().getAnno());
		atto.setStatoOperativo(StatoOperativoAtti.DEFINITIVO);
		atto.setOggetto(getAllegatoAtto().getCausale());
		
		request.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		request.setAttoAmministrativo(atto);
		request.setTipoAtto(getTipoAttoALG());
		request.setEnte(getEnte());
		
		return request;
	}

	
}
