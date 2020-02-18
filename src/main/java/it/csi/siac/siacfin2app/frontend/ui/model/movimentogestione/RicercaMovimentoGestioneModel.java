/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.movimentogestione;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.MovimentoGestioneHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * Classe di model per la ricerca dei movimenti di gestione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/03/2014
 *
 */
public class RicercaMovimentoGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4705404726637361367L;

	private Impegno impegno;
	private Accertamento accertamento;
	private BigDecimal numeroSubmovimentoGestione; 
	private AttoAmministrativo attoAmministrativo;
	private List<Impegno> listaImpegni;
	
	//integrazione con il servizio ricercaImpegniPerChiaveOttimizzato
	//inizializzato a true in analogia al valore di default del servizio
	private boolean caricaSub = true;
	private StatoOperativoMovimentoGestione statoOperativoMovimentoGestioneDaFiltrare = StatoOperativoMovimentoGestione.DEFINITIVO;
	private boolean escludiSubAnnullati = true; 
	private boolean subPaginati = true;
	//SIAC-6255
	private boolean mostraErroreNessunDatoTrovato = true;
	private boolean caricaDisponibilitaLiquidare = true;
	private Progetto progetto;
	private Cronoprogramma cronoprogramma;
	
	/** Costruttore vuoto di default */
	public RicercaMovimentoGestioneModel() {
		super();
	}
	
	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
	
	
	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	/**
	 * @return the listaImpegni
	 */
	public List<Impegno> getListaImpegni() {
		return listaImpegni;
	}

	/**
	 * @param listaImpegni the listaImpegni to set
	 */
	public void setListaImpegni(List<Impegno> listaImpegni) {
		this.listaImpegni = listaImpegni;
	}
	
	/**
	 * @return the caricaSub
	 */
	public boolean getCaricaSub() {
		return caricaSub;
	}

	/**
	 * @param caricaSub the caricaSub to set
	 */
	public void setCaricaSub(boolean caricaSub) {
		this.caricaSub = caricaSub;
	}
	

	/**
	 * @return the StatoOperativoMovimentoGestione
	 */
	public StatoOperativoMovimentoGestione getStatoOperativoMovimentoGestioneDaFiltrare() {
		return statoOperativoMovimentoGestioneDaFiltrare;
	}

	/**
	 * @param statoOperativoMovimentoGestioneDaFiltrare the statoOperativoMovimentoGestioneDaFiltrare to set
	 */
	public void setStatoOperativoMovimentoGestioneDaFiltrare(StatoOperativoMovimentoGestione statoOperativoMovimentoGestioneDaFiltrare) {
		this.statoOperativoMovimentoGestioneDaFiltrare = statoOperativoMovimentoGestioneDaFiltrare;
	}

	/**
	 * @return the escludi annullati
	 */
	public boolean isEscludiSubAnnullati() {
		return escludiSubAnnullati;
	}

	/**
	 * @param escludiAnnullati the escludiAnnullatiToSet
	 */
	public void setEscludiSubAnnullati(boolean escludiAnnullati) {
		this.escludiSubAnnullati = escludiAnnullati;
	}
	
	/**
	 * @return the subPaginati
	 */
	public boolean isSubPaginati() {
		return subPaginati;
	}

	/**
	 * @param subPaginati the subPaginati to set
	 */
	public void setSubPaginati(boolean subPaginati) {
		this.subPaginati = subPaginati;
	}

	/**
	 * @return the numero submovimento gestione
	 */
	public BigDecimal getNumeroSubmovimentoGestione() {
		return numeroSubmovimentoGestione;
	}

	/**
	 * @param numeroSubmovimentoGestione the numeroSubMovimentoGestione to set
	 */
	public void setNumeroSubmovimentoGestione(BigDecimal numeroSubmovimentoGestione) {
		this.numeroSubmovimentoGestione = numeroSubmovimentoGestione;
	}
	
	/**
	 * @return the mostraErroreNessunDatoTrovato
	 */
	public boolean isMostraErroreNessunDatoTrovato() {
		return mostraErroreNessunDatoTrovato;
	}

	/**
	 * @param mostraErroreNessunDatoTrovato the mostraErroreNessunDatoTrovato to set
	 */
	public void setMostraErroreNessunDatoTrovato(boolean mostraErroreNessunDatoTrovato) {
		this.mostraErroreNessunDatoTrovato = mostraErroreNessunDatoTrovato;
	}
	
	/**
	 * @return the caricaDisponibilitaLiquidare
	 */
	public boolean isCaricaDisponibilitaLiquidare() {
		return caricaDisponibilitaLiquidare;
	}

	/**
	 * @param caricaDisponibilitaLiquidare the caricaDisponibilitaLiquidare to set
	 */
	public void setCaricaDisponibilitaLiquidare(boolean caricaDisponibilitaLiquidare) {
		this.caricaDisponibilitaLiquidare = caricaDisponibilitaLiquidare;
	}

	/**
	 * @return the progetto
	 */
	public Progetto getProgetto() {
		return progetto;
	}

	/**
	 * @param progetto the progetto to set
	 */
	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

	/**
	 * @return the cronoprogramma
	 */
	public Cronoprogramma getCronoprogramma() {
		return cronoprogramma;
	}

	/**
	 * @param cronoprogramma the cronoprogramma to set
	 */
	public void setCronoprogramma(Cronoprogramma cronoprogramma) {
		this.cronoprogramma = cronoprogramma;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @return la request creata
	 * @deprecated Utilizzare la ricerca Ottimizzata
	 */
	@Deprecated
	public RicercaImpegnoPerChiave creaRequestRicercaImpegnoPerChiave() {
		RicercaImpegnoPerChiave request = new RicercaImpegnoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setpRicercaImpegnoK(creaRicercaImpegnoK());
		
		return request;
	}


	/**
	 * Crea un'utility per la ricerca dell'Impegno.
	 * 
	 * @return l'utility creata
	 */
	private RicercaImpegnoK creaRicercaImpegnoK() {
		RicercaImpegnoK utility = new RicercaImpegnoK();
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoImpegno(impegno.getAnnoMovimento());
		utility.setNumeroImpegno(impegno.getNumero());
		
		return utility;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * 
	 * @return la request creata
	 * @deprecated Utilizzare la ricerca Ottimizzata
	 */
	@Deprecated
	public RicercaAccertamentoPerChiave creaRequestRicercaAccertamentoPerChiave() {
		RicercaAccertamentoPerChiave request = new RicercaAccertamentoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setpRicercaAccertamentoK(creaRicercaAccertamentoK());
		
		return request;
	}

	/**
	 * Crea un'utility per la ricerca dell'Accertamento.
	 * 
	 * @return l'utility creata
	 */
	private RicercaAccertamentoK creaRicercaAccertamentoK() {
		RicercaAccertamentoK utility = new RicercaAccertamentoK();
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoAccertamento(accertamento.getAnnoMovimento());
		utility.setNumeroAccertamento(accertamento.getNumero());
		
		return utility;
	}

	/**
	 * crea la request per la ricerca sintetica 
	 * @date 14/01/2015 
	 * @author Ahmad Nazha
	 * @return la request per il servizio
	 */
	public RicercaSinteticaImpegniSubImpegni creaRequestRicercaSinteticaImpegniSubImpegni() {
		RicercaSinteticaImpegniSubImpegni request = creaPaginazioneRequest(RicercaSinteticaImpegniSubImpegni.class);
		request.setEnte(getEnte());
		
		ParametroRicercaImpSub pr = new ParametroRicercaImpSub();
		pr.setAnnoEsercizio(getAnnoEsercizioInt());
		
		/* ***IMPEGNO*** */
		if(impegno != null) {
			pr.setAnnoImpegno(impegno.getAnnoMovimento() != 0 ? Integer.valueOf(impegno.getAnnoMovimento()) : null);
			pr.setNumeroImpegno(impegno.getNumero() != null ? Integer.valueOf(impegno.getNumero().intValue()) : null);
		}
		
		/* ***PROVVEDIMENTO*** */
		if(attoAmministrativo != null) {
			pr.setAnnoProvvedimento(attoAmministrativo.getAnno() != 0 ? Integer.valueOf(attoAmministrativo.getAnno()) : null);
			pr.setNumeroProvvedimento(attoAmministrativo.getNumero() != 0 ? Integer.valueOf(attoAmministrativo.getNumero()) : null);
			pr.setTipoProvvedimento(impostaEntitaFacoltativa(attoAmministrativo.getTipoAtto()));
			pr.setUidStrutturaAmministrativoContabile(idEntitaPresente(attoAmministrativo.getStrutturaAmmContabile()) ? Integer.valueOf(attoAmministrativo.getStrutturaAmmContabile().getUid()) : null);
		}
		
		if(progetto != null && StringUtils.isNotEmpty(progetto.getCodice())) {
			pr.setProgetto(progetto.getCodice());
			pr.setCodiceProgetto(progetto.getCodice());
		}
		
		if(cronoprogramma != null && cronoprogramma.getUid() != 0) {
			pr.setUidCronoprogramma(cronoprogramma.getUid());
		}
		pr.setCaricaDisponibilitaALiquidare(Boolean.valueOf(isCaricaDisponibilitaLiquidare()));
		request.setParametroRicercaImpSub(pr);
		
		return request;
	}
	
	/**
	 * crea la request per la ricerca sintetica 
	 * @date 14/01/2015 
	 * @author Ahmad Nazha
	 * @return la request per il servizio
	 */
	public RicercaSinteticaAccertamentiSubAccertamenti creaRequestRicercaSinteticaAccertamentiSubAccertamenti() {
		RicercaSinteticaAccertamentiSubAccertamenti request = creaPaginazioneRequest(RicercaSinteticaAccertamentiSubAccertamenti.class);
		request.setEnte(getEnte());
		
		ParametroRicercaAccSubAcc pr= new ParametroRicercaAccSubAcc();
		pr.setAnnoEsercizio(getAnnoEsercizioInt());
		
		/* ***ACCERTAMENTO*** */
		if(accertamento != null) {
			pr.setAnnoAccertamento(accertamento.getAnnoMovimento() != 0 ? Integer.valueOf(accertamento.getAnnoMovimento()) : null);
			pr.setNumeroAccertamento(accertamento.getNumero() != null ? Integer.valueOf(accertamento.getNumero().intValue()) : null);
		}
	
		/* ***PROVVEDIMENTO*** */
		if(attoAmministrativo != null) {
			pr.setAnnoProvvedimento(attoAmministrativo.getAnno() != 0 ? Integer.valueOf(attoAmministrativo.getAnno()) : null);
			pr.setNumeroProvvedimento(attoAmministrativo.getNumero() != 0 ? Integer.valueOf(attoAmministrativo.getNumero()) : null);
			pr.setTipoProvvedimento(impostaEntitaFacoltativa(attoAmministrativo.getTipoAtto()));
			pr.setUidStrutturaAmministrativoContabile(idEntitaPresente(attoAmministrativo.getStrutturaAmmContabile()) ? Integer.valueOf(attoAmministrativo.getStrutturaAmmContabile().getUid()) : null);
		}
	
		request.setParametroRicercaAccSubAcc(pr);
		
		return request;
	}

	/**
	 *  Crea una request per il servizio {@link RicercaImpegnoPerChiaveOttimizzato}}
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		request.setpRicercaImpegnoK(creaRicercaImpegnoK());
		
		//il default e' carica sub -> true
		request.setCaricaSub(getCaricaSub());
		request.setSubPaginati(isSubPaginati());
		request.setEscludiSubAnnullati(isEscludiSubAnnullati());
		request.setFiltroSubSoloInQuestoStato(getStatoOperativoMovimentoGestioneDaFiltrare().getCodice());
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(isEscludiSubAnnullati());
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO, TipologiaClassificatore.MACROAGGREGATO));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);

		return request;
	}
	
	/**
	 *  Crea una request per il servizio {@link RicercaImpegnoPerChiaveOttimizzato}}
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoSubimpegnoPerChiaveOttimizzato() {
		//SIAC-4560
		SubImpegno subImpegno = new SubImpegno();
		subImpegno.setNumero(getNumeroSubmovimentoGestione());
		return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), impegno, subImpegno);
	}
	
	/**
	 *  Crea una request per il servizio {@link RicercaAccertamentoPerChiaveOttimizzato}}
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		
		request.setpRicercaAccertamentoK(creaRicercaAccertamentoK());
		
		//il default e' carica sub -> true
		request.setCaricaSub(getCaricaSub());
		request.setSubPaginati(isSubPaginati());
		request.setEscludiSubAnnullati(isEscludiSubAnnullati());
		request.setFiltroSubSoloInQuestoStato(statoOperativoMovimentoGestioneDaFiltrare.getCodice());
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(isEscludiSubAnnullati());
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		return request;
	}
	
	/**
	 *  Crea una request per il servizio {@link RicercaAccertamentoPerChiaveOttimizzato}}
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoSubAccertamentoPerChiaveOttimizzato() {
		//SIAC-4560
		SubAccertamento subAccertamento = new SubAccertamento();
		subAccertamento.setNumero(getNumeroSubmovimentoGestione());
		return MovimentoGestioneHelper.creaRequestRicercaAccertamentoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), accertamento, subAccertamento);
	}
	
}
