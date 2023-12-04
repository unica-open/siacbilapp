/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;
import java.util.EnumSet;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * Classe astratta di model per la Causale.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 16/04/2014
 *
 */
public class GenericCausaleSpesaModel extends GenericCausaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4948730122493670234L;
	
	private CausaleSpesa causale;
	
	private Integer idCausale;
	
	private Impegno movimentoGestione;
	private SubImpegno subMovimentoGestione;
	
	private CapitoloUscitaGestione capitolo;
	
	/** Costruttore vuoto di default */
	public GenericCausaleSpesaModel() {
		super();
		setNomeAzioneDecentrata(BilConstants.GESTIONE_CAUSALI_PREDOCUMENTO_SPESA_DECENTRATO.getConstant());
	}
	
	/**
	 * @return the causale
	 */
	public CausaleSpesa getCausale() {
		return causale;
	}

	/**
	 * @param causale the causale to set
	 */
	public void setCausale(CausaleSpesa causale) {
		this.causale = causale;
	}

	/**
	 * @return the idCausale
	 */
	public Integer getIdCausale() {
		return idCausale;
	}

	/**
	 * @param idCausale the idCausale to set
	 */
	public void setIdCausale(Integer idCausale) {
		this.idCausale = idCausale;
	}
	
	/**
	 * @return the movimentoGestione
	 */
	public Impegno getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(Impegno movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubImpegno getSubMovimentoGestione() {
		return subMovimentoGestione;
	}
	
	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(SubImpegno subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}
	
	/**
	 * @return the capitolo
	 */
	public CapitoloUscitaGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(CapitoloUscitaGestione capitolo) {
		this.capitolo = capitolo;
	}

	/* Request */
	
	/**
	 * Crea una request per il servizio di Ricerca di dettaglio della Causale.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCausaleSpesa creaRequestRicercaDettaglioCausaleSpesa() {
		RicercaDettaglioCausaleSpesa request = new RicercaDettaglioCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setCausaleSpesa(causale);
		
		return request;
	}
	
	/**
	 * Imposta i dati del Causale di Spesa all'interno del model.
	 *  
	 * @param causaleSpesa il predocumento i cui dati devono essere impostati
	 */
	public void impostaCausale(CausaleSpesa causaleSpesa) {
		setCausale(causaleSpesa);
		setCapitolo(causaleSpesa.getCapitoloUscitaGestione());
		setSoggetto(causaleSpesa.getSoggetto());
		setMovimentoGestione(causaleSpesa.getImpegno());
		setSubMovimentoGestione(causaleSpesa.getSubImpegno());
	
		setSedeSecondariaSoggetto(causaleSpesa.getSedeSecondariaSoggetto());
		setModalitaPagamentoSoggetto(causaleSpesa.getModalitaPagamentoSoggetto());
		setStrutturaAmministrativoContabile(causaleSpesa.getStrutturaAmministrativoContabile());
		setTipoCausale(causaleSpesa.getTipoCausale());
		
		impostaAttoAmministrativo(causaleSpesa.getAttoAmministrativo());
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCapitoloUscitaGestione creaRequestRicercaSinteticaCapitoloUscitaGestione() {
		RicercaSinteticaCapitoloUscitaGestione request = new RicercaSinteticaCapitoloUscitaGestione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione(10000));
		request.setRicercaSinteticaCapitoloUGest(creaRicercaSinteticaCapitoloUGest());
		
		return request;
	}
	
	/**
	 * Crea un'utilit&agrave; per la ricerca sintetica Capitolo Spesa.
	 * 
	 * @return l'utility creata
	 */
	private RicercaSinteticaCapitoloUGest creaRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
		utility.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
		utility.setNumeroArticolo(getCapitolo().getNumeroArticolo());
		utility.setNumeroUEB(getCapitolo().getNumeroUEB());
		
		return utility;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		request.setpRicercaImpegnoK(creaPRicercaImpegnoK());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
	
	/* Utility */
	
	/**
	 * Crea un'utilit&agrave; per la ricerca impegno.
	 * 
	 * @return l'utility creata
	 */
	private RicercaImpegnoK creaPRicercaImpegnoK() {
		RicercaImpegnoK utility = new RicercaImpegnoK();
		if(getMovimentoGestione() != null) {
			utility.setAnnoEsercizio(getAnnoEsercizioInt());
			utility.setAnnoImpegno(getMovimentoGestione().getAnnoMovimento());
			utility.setNumeroImpegno(getMovimentoGestione().getNumeroBigDecimal());
		}
		if(getSubMovimentoGestione() != null) {
			utility.setNumeroSubDaCercare(getSubMovimentoGestione().getNumeroBigDecimal());
		}
		
		return utility;
	}
	
}
