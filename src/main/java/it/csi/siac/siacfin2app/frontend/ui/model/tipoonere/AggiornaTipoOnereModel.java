/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.tipoonere;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionBilUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnere;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoOnereModelDetail;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;


/**
 * Classe di model per l'aggiornamento del Tipo Onere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
public class AggiornaTipoOnereModel extends GenericTipoOnereModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5429827569724173948L;

	private Integer uidTipoOnere;
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	
	private List<AttivitaOnere> attivitaOnere = new ArrayList<AttivitaOnere>();
	private List<Causale770> causali770 = new ArrayList<Causale770>();
	private List<CodiceSommaNonSoggetta> sommeNonSoggette =  new ArrayList<CodiceSommaNonSoggetta>();
	private List<CausaleSpesa> listaCausaleSpesa = new ArrayList<CausaleSpesa>();
	private List<CausaleEntrata> listaCausaleEntrata = new ArrayList<CausaleEntrata>();
	
	private List<CausaleSpesa> listaCausaleSpesaDaEliminare = new ArrayList<CausaleSpesa>();
	private List<CausaleEntrata> listaCausaleEntrataDaEliminare = new ArrayList<CausaleEntrata>();
	
	private CapitoloEntrataGestione capitoloEntrataGestione;
	private CapitoloUscitaGestione capitoloUscitaGestione;
	
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	private Impegno impegno;
	private SubImpegno subImpegno;
	
	private Soggetto soggetto;
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCessione;
	
	private Integer row;
	
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	
	private boolean capitoloUscitaNonEditabile;
	private boolean capitoloEntrataNonEditabile;
	private boolean soggettoNonEditabile;
	
	private CausaleSpesa causaleSpesaSoloCapitolo;
	private CausaleSpesa causaleSpesaSoloSoggetto;
	private CausaleEntrata causaleEntrataSoloCapitolo;
	
	//SIAC-5060
	private Distinta distinta;
	private List<CodificaFin> listaDistinta = new ArrayList<CodificaFin>();
	
	private Date now = new Date();

	/** Costruttore vuoto di default */
	public AggiornaTipoOnereModel() {
		setTitolo("Aggiorna onere");
	}

	/**
	 * @return the uidTipoOnere
	 */
	public Integer getUidTipoOnere() {
		return uidTipoOnere;
	}

	/**
	 * @param uidTipoOnere the uidTipoOnere to set
	 */
	public void setUidTipoOnere(Integer uidTipoOnere) {
		this.uidTipoOnere = uidTipoOnere;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}

	/**
	 * @return the listaTipiFinanziamento
	 */
	public List<TipoFinanziamento> getListaTipiFinanziamento() {
		return listaTipiFinanziamento;
	}

	/**
	 * @param listaTipiFinanziamento the listaTipiFinanziamento to set
	 */
	public void setListaTipiFinanziamento(List<TipoFinanziamento> listaTipiFinanziamento) {
		this.listaTipiFinanziamento = listaTipiFinanziamento != null ? listaTipiFinanziamento : new ArrayList<TipoFinanziamento>();
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
	
	/**
	 * @return the listaCausaleSpesa
	 */
	public List<CausaleSpesa> getListaCausaleSpesa() {
		return listaCausaleSpesa;
	}

	/**
	 * @param listaCausaleSpesa the listaCausaleSpesa to set
	 */
	public void setListaCausaleSpesa(List<CausaleSpesa> listaCausaleSpesa) {
		this.listaCausaleSpesa = listaCausaleSpesa != null ? listaCausaleSpesa : new ArrayList<CausaleSpesa>();
	}

	/**
	 * @return the listaCausaleEntrata
	 */
	public List<CausaleEntrata> getListaCausaleEntrata() {
		return listaCausaleEntrata;
	}

	/**
	 * @param listaCausaleEntrata the listaCausaleEntrata to set
	 */
	public void setListaCausaleEntrata(List<CausaleEntrata> listaCausaleEntrata) {
		this.listaCausaleEntrata = listaCausaleEntrata != null ? listaCausaleEntrata : new ArrayList<CausaleEntrata>();
	}

	/**
	 * @return the listaCausaleSpesaDaEliminare
	 */
	public List<CausaleSpesa> getListaCausaleSpesaDaEliminare() {
		return listaCausaleSpesaDaEliminare;
	}

	/**
	 * @param listaCausaleSpesaDaEliminare the listaCausaleSpesaDaEliminare to set
	 */
	public void setListaCausaleSpesaDaEliminare(List<CausaleSpesa> listaCausaleSpesaDaEliminare) {
		this.listaCausaleSpesaDaEliminare = listaCausaleSpesaDaEliminare != null ? listaCausaleSpesaDaEliminare : new ArrayList<CausaleSpesa>();
	}

	/**
	 * @return the listaCausaleEntrataDaEliminare
	 */
	public List<CausaleEntrata> getListaCausaleEntrataDaEliminare() {
		return listaCausaleEntrataDaEliminare;
	}

	/**
	 * @param listaCausaleEntrataDaEliminare the listaCausaleEntrataDaEliminare to set
	 */
	public void setListaCausaleEntrataDaEliminare(List<CausaleEntrata> listaCausaleEntrataDaEliminare) {
		this.listaCausaleEntrataDaEliminare = listaCausaleEntrataDaEliminare != null ? listaCausaleEntrataDaEliminare : new ArrayList<CausaleEntrata>();
	}

	/**
	 * @return the capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestione() {
		return capitoloEntrataGestione;
	}

	/**
	 * @param capitoloEntrataGestione the capitoloEntrataGestione to set
	 */
	public void setCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
	}

	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * @param capitoloUscitaGestione the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
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
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}

	/**
	 * @param subAccertamento the subAccertamento to set
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
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
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}
	
	/**
	 * Ottiene la descrizione completa del soggetto per la visualizzazione in pagina.
	 * 
	 * @return the descrizioneCompletaSoggetto
	 */
	public String getDescrizioneCompletaSoggetto() {
		return computeDescrizioneCompletaSoggetto(getSoggetto());
	}

	/**
	 * Ottiene la descrizione completa dell'indirizzo della sede secondaria per la visualizzazione nella colonna Indirizzo in pagina.
	 * 
	 * @return the indirizzoCompletoSedeSecondariaSoggetto
	 */
	public String getIndirizzoCompletoSedeSecondariaSoggetto() {
		return computeIndirizzoCompletoSedeSecondariaSoggetto(getSedeSecondariaSoggetto());
	}
	
	/**
	 * Ottiene la descrizione completa della modalit&agrave; di pagamento per la visualizzazione nella colonna Modalit&agrave; in pagina.
	 * 
	 * @return the modalitaPagamentoCompletaSoggetto
	 */
	public String getModalitaPagamentoCompletaSoggetto() {
		return computeModalitaPagamentoCompletaSoggetto(getModalitaPagamentoSoggetto());
	}

	/**
	 * @return the sedeSecondariaSoggetto
	 */
	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	/**
	 * @param sedeSecondariaSoggetto the sedeSecondariaSoggetto to set
	 */
	public void setSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}
	
	/**
	 * @return the modalitaPagamentoSoggettoCessione
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoCessione() {
		return modalitaPagamentoSoggettoCessione;
	}

	/**
	 * @param modalitaPagamentoSoggettoCessione the modalitaPagamentoSoggettoCessione to set
	 */
	public void setModalitaPagamentoSoggettoCessione(ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCessione) {
		this.modalitaPagamentoSoggettoCessione = modalitaPagamentoSoggettoCessione;
	}

	/**
	 * @return the row
	 */
	public Integer getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}

	/**
	 * @return the listaSedeSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}

	/**
	 * @param listaSedeSecondariaSoggetto the listaSedeSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = listaSedeSecondariaSoggetto != null ? listaSedeSecondariaSoggetto : new ArrayList<SedeSecondariaSoggetto>();
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto != null ? listaModalitaPagamentoSoggetto : new ArrayList<ModalitaPagamentoSoggetto>();
	}
	

	/**
	 * @return the capitoloUscitaNonEditabile
	 */
	public boolean isCapitoloUscitaNonEditabile() {
		return capitoloUscitaNonEditabile;
	}

	/**
	 * @param capitoloUscitaNonEditabile the capitoloUscitaNonEditabile to set
	 */
	public void setCapitoloUscitaNonEditabile(boolean capitoloUscitaNonEditabile) {
		this.capitoloUscitaNonEditabile = capitoloUscitaNonEditabile;
	}

	/**
	 * @return the capitoloEntrataNonEditabile
	 */
	public boolean isCapitoloEntrataNonEditabile() {
		return capitoloEntrataNonEditabile;
	}

	/**
	 * @param capitoloEntrataNonEditabile the capitoloEntrataNonEditabile to set
	 */
	public void setCapitoloEntrataNonEditabile(boolean capitoloEntrataNonEditabile) {
		this.capitoloEntrataNonEditabile = capitoloEntrataNonEditabile;
	}
	
	/**
	 * @return the soggettoNonEditabile
	 */
	public boolean isSoggettoNonEditabile() {
		return soggettoNonEditabile;
	}

	/**
	 * @param soggettoNonEditabile the soggettoNonEditabile to set
	 */
	public void setSoggettoNonEditabile(boolean soggettoNonEditabile) {
		this.soggettoNonEditabile = soggettoNonEditabile;
	}

	/**
	 * @return the causaleSpesaSoloCapitolo
	 */
	public CausaleSpesa getCausaleSpesaSoloCapitolo() {
		return causaleSpesaSoloCapitolo;
	}

	/**
	 * @param causaleSpesaSoloCapitolo the causaleSpesaSoloCapitolo to set
	 */
	public void setCausaleSpesaSoloCapitolo(CausaleSpesa causaleSpesaSoloCapitolo) {
		this.causaleSpesaSoloCapitolo = causaleSpesaSoloCapitolo;
	}
	
	/**
	 * @return the causaleSpesaSoloSoggetto
	 */
	public CausaleSpesa getCausaleSpesaSoloSoggetto() {
		return causaleSpesaSoloSoggetto;
	}

	/**
	 * @param causaleSpesaSoloSoggetto the causaleSpesaSoloSoggetto to set
	 */
	public void setCausaleSpesaSoloSoggetto(CausaleSpesa causaleSpesaSoloSoggetto) {
		this.causaleSpesaSoloSoggetto = causaleSpesaSoloSoggetto;
	}

	/**
	 * @return the causaleEntrataSoloCapitolo
	 */
	public CausaleEntrata getCausaleEntrataSoloCapitolo() {
		return causaleEntrataSoloCapitolo;
	}

	/**
	 * @param causaleEntrataSoloCapitolo the causaleEntrataSoloCapitolo to set
	 */
	public void setCausaleEntrataSoloCapitolo(
			CausaleEntrata causaleEntrataSoloCapitolo) {
		this.causaleEntrataSoloCapitolo = causaleEntrataSoloCapitolo;
	}

	/**
	 * @return the distinta
	 */
	public Distinta getDistinta() {
		return distinta;
	}

	/**
	 * @param distinta the distinta to set
	 */
	public void setDistinta(Distinta distinta) {
		this.distinta = distinta;
	}

	/**
	 * @return the listaDistinta
	 */
	public List<CodificaFin> getListaDistinta() {
		return listaDistinta;
	}

	/**
	 * @param listaDistinta the listaDistinta to set
	 */
	public void setListaDistinta(List<CodificaFin> listaDistinta) {
		this.listaDistinta = listaDistinta;
	}

	/**
	 * @return the listaSedeSecondariaSoggettoValide
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggettoValide() {
		List<SedeSecondariaSoggetto> lista = new ArrayList<SedeSecondariaSoggetto>();
		for(SedeSecondariaSoggetto sss : listaSedeSecondariaSoggetto) {
			if(StatoOperativoSedeSecondaria.VALIDO.equals(sss.getStatoOperativoSedeSecondaria())
					|| StatoOperativoSedeSecondaria.SOSPESO.equals(sss.getStatoOperativoSedeSecondaria())) {
				lista.add(sss);
			}
		}
		return lista;
	}
	
	/**
	 * @return the listaModalitaPagamentoSoggettoFiltrate
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoFiltrate() {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>(getListaModalitaPagamentoSoggetto());
		if(getSedeSecondariaSoggetto() != null && getSedeSecondariaSoggetto().getUid() != 0) {
			SedeSecondariaSoggetto sss = ComparatorUtils.searchByUid(getListaSedeSecondariaSoggettoValide(), getSedeSecondariaSoggetto());
			String denominazione = StringUtils.trimToEmpty(sss.getDenominazione());
			for(Iterator<ModalitaPagamentoSoggetto> iterator = result.iterator(); iterator.hasNext();) {
				ModalitaPagamentoSoggetto mps = iterator.next();
				if(!StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equals(mps.getCodiceStatoModalitaPagamento())
						|| (!denominazione.equalsIgnoreCase(mps.getAssociatoA()))) {
					iterator.remove();
				}
			}
		}
		return result;
	}
	
	/**
	 * @return the totaleCausaliCollegate
	 */
	public Long getTotaleCausaliCollegate() {
		return Long.valueOf(getListaCausaleSpesa().size() + getListaCausaleEntrata().size());
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link AggiornaTipoOnere}.
	 * 
	 * @return la request creata
	 */
	public AggiornaTipoOnere creaRequestAggiornaTipoOnere() {
		AggiornaTipoOnere request = creaRequest(AggiornaTipoOnere.class);
		
		// Popolo il TipoOnere
		// Aggiungo le causali 770, di entrate e di spesa
		popolaCausaliTipoOnere();
		getTipoOnere().setAttivitaOnere(getAttivitaOnere());
		getTipoOnere().setCodiciSommaNonSoggetta(getSommeNonSoggette());
		
		request.setTipoOnere(getTipoOnere());
		request.setAnnoEsercizio(getAnnoEsercizioInt());
		
		return request;
	}
	
	/**
	 * Popolamento delle causali del tipoOnere.
	 */
	private void popolaCausaliTipoOnere() {
		List<Causale> list = getTipoOnere().getCausali();
		// Pulisco le causali associate
		list.clear();
		// Aggiungo le 770
		list.addAll(getCausali770());
		// Imposto i dati nelle causali
		impostaSoggettoECapitoloEntrata();
		impostaSoggettoECapitoloSpesa();
		
		// Aggiungo le causali ancora presenti
		getTipoOnere().getCausali().addAll(getListaCausaleEntrata());
		getTipoOnere().getCausali().addAll(getListaCausaleSpesa());
		// Aggiungo quelle da eliminare
		getTipoOnere().getCausali().addAll(getListaCausaleEntrataDaEliminare());
		getTipoOnere().getCausali().addAll(getListaCausaleSpesaDaEliminare());
	}

	/**
	 * Imposta il soggetto e il capitolo nella lista delle causali di entrata.
	 */
	private void impostaSoggettoECapitoloEntrata() {
		CapitoloEntrataGestione ceg = ReflectionBilUtil.createWithOnlyUid(getCapitoloEntrataGestione());
		// SIAC-5147: Devo reimpostare almeno anno capitolo
		if(ceg != null && getCapitoloEntrataGestione() != null) {
			ceg.setAnnoCapitolo(getCapitoloEntrataGestione().getAnnoCapitolo());
		}
		
		Soggetto s = ReflectionBilUtil.createWithOnlyUid(getSoggetto());
		for(CausaleEntrata c : getListaCausaleEntrata()) {
			c.setCapitoloEntrataGestione(ceg);
			c.setSoggetto(s);
		}
		
//		 Aggiungo una causale con solo il capitolo se nessuna causale e' stata selezionata
		if(getListaCausaleEntrata().isEmpty() && ceg != null) {
			CausaleEntrata causaleEntrata = new CausaleEntrata();
			causaleEntrata.setCapitoloEntrataGestione(ceg);
			getListaCausaleEntrata().add(causaleEntrata);
		}
		//se non ci sono causali e il capitolo non è stato popolato elimino la causale con solo capitolo già presente
		if(getListaCausaleEntrata().isEmpty() && ceg == null && causaleEntrataSoloCapitolo != null){
			eliminaCausaleEntrata(causaleEntrataSoloCapitolo);
		}
	}


	/**
	 * Imposta il soggetto e il capitolo nella lista delle causali di spesa.
	 */
	private void impostaSoggettoECapitoloSpesa() {
		CapitoloUscitaGestione cug = ReflectionBilUtil.createWithOnlyUid(getCapitoloUscitaGestione());
		// SIAC-5147: Devo reimpostare almeno anno capitolo
		if(cug != null && getCapitoloUscitaGestione() != null) {
			cug.setAnnoCapitolo(getCapitoloUscitaGestione().getAnnoCapitolo());
		}
		Soggetto s = ReflectionBilUtil.createWithOnlyUid(getSoggetto());
		SedeSecondariaSoggetto sss = impostaEntitaFacoltativa(getSedeSecondariaSoggetto());
		
		// Modalita di pagamento: provo prima con il cessione, se non ho dati prendo l'originale
		ModalitaPagamentoSoggetto mps = impostaEntitaFacoltativa(getModalitaPagamentoSoggettoCessione());
		if(mps == null) {
			mps = impostaEntitaFacoltativa(getModalitaPagamentoSoggetto());
		}
		
		for(CausaleSpesa c : getListaCausaleSpesa()) {
			c.setSoggetto(s);
			c.setSedeSecondariaSoggetto(sss);
			c.setModalitaPagamentoSoggetto(mps);
			c.setCapitoloUscitaGestione(cug);
		}
		List<CausaleSpesa> causaliDaAggiungere = new ArrayList<CausaleSpesa>();
		// Aggiungo una causale con solo il soggetto o il capitolo se nessuna causale e' stata selezionata
		if(getListaCausaleSpesa().isEmpty() && cug != null) {
			CausaleSpesa causaleSpesa = new CausaleSpesa();
			causaleSpesa.setCapitoloUscitaGestione(cug);
			causaliDaAggiungere.add(causaleSpesa);
		}
		// Aggiungo una causale con solo il soggetto o il capitolo se nessuna causale e' stata selezionata
		if(getListaCausaleSpesa().isEmpty() && s != null) {
			CausaleSpesa causaleSpesa = new CausaleSpesa();
			causaleSpesa.setSoggetto(s);
			causaleSpesa.setSedeSecondariaSoggetto(sss);
			causaleSpesa.setModalitaPagamentoSoggetto(mps);
			causaliDaAggiungere.add(causaleSpesa);
		}
		//se non ci sono causali e il capitolo non è stato popolato elimino la causale con solo capitolo già presente
		if(getListaCausaleSpesa().isEmpty() && cug == null && causaleSpesaSoloCapitolo != null){
			eliminaCausaleSpesa(causaleSpesaSoloCapitolo);
			
		}
		//se non ci sono causali e il soggetto non è stato popolato elimino la causale con solo soggetto già presente
		if(getListaCausaleSpesa().isEmpty() && s == null && causaleSpesaSoloSoggetto != null){
			eliminaCausaleSpesa(causaleSpesaSoloSoggetto);
		}
		getListaCausaleSpesa().addAll(causaliDaAggiungere);
	}

	private void eliminaCausaleSpesa(CausaleSpesa causaleSpesa) {
		causaleSpesa.setDataFineValidita(now);
		causaleSpesa.setDataScadenza(now);
		causaleSpesa.setDataFineValiditaCausale(now);
		causaleSpesa.setAttoAmministrativo(null);
		causaleSpesa.setCapitoloUscitaGestione(null);
		causaleSpesa.setSubImpegno(null);
		causaleSpesa.setImpegno(null);
		causaleSpesa.setSedeSecondariaSoggetto(null);
		causaleSpesa.setSoggetto(null);
		causaleSpesa.setStrutturaAmministrativoContabile(null);
		causaleSpesa.setModalitaPagamentoSoggetto(null);
		getListaCausaleSpesaDaEliminare().add(causaleSpesa);
	}
	
	private void eliminaCausaleEntrata(CausaleEntrata causaleEntrata) {
		causaleEntrata.setDataFineValidita(now);
		causaleEntrata.setDataScadenza(now);
		causaleEntrata.setDataFineValiditaCausale(now);
		causaleEntrata.setAccertamento(null);
		causaleEntrata.setAttoAmministrativo(null);
		causaleEntrata.setCapitoloEntrataGestione(null);
		causaleEntrata.setSoggetto(null);
		causaleEntrata.setStrutturaAmministrativoContabile(null);
		causaleEntrata.setSubAccertamento(null);
		getListaCausaleEntrataDaEliminare().add(causaleEntrata);
		
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioTipoOnere}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioTipoOnere creaRequestRicercaDettaglioTipoOnere() {
		RicercaDettaglioTipoOnere request = creaRequest(RicercaDettaglioTipoOnere.class);
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(getUidTipoOnere());
		request.setTipoOnere(tipoOnere);
		
		request.setTipoOnereModelDetails(TipoOnereModelDetail.Attr, TipoOnereModelDetail.Causali, TipoOnereModelDetail.Attivita);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class, 1);
		
		request.setEnte(getEnte());
		request.setCaricaSub(getSubAccertamento() != null && getSubAccertamento().getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoAccertamento(getAccertamento().getAnnoMovimento());
		pRicercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
		pRicercaAccertamentoK.setNumeroAccertamento(getAccertamento().getNumeroBigDecimal());
		pRicercaAccertamentoK.setNumeroSubDaCercare(getSubAccertamento() != null ? getSubAccertamento().getNumeroBigDecimal() : null);
		request.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		
		request.setEnte(getEnte());
		request.setCaricaSub(getSubImpegno() != null && getSubImpegno().getNumeroBigDecimal() != null);
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
		
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		pRicercaImpegnoK.setAnnoImpegno(getImpegno().getAnnoMovimento());
		pRicercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
		pRicercaImpegnoK.setNumeroImpegno(getImpegno().getNumeroBigDecimal());
		pRicercaImpegnoK.setNumeroSubDaCercare(getSubImpegno() != null ? getSubImpegno().getNumeroBigDecimal() : null);
		request.setpRicercaImpegnoK(pRicercaImpegnoK);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaPuntualeCapitoloEntrataGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEntrataGestione request = creaRequest(RicercaPuntualeCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		
		RicercaPuntualeCapitoloEGest ricercaPuntualeCapitoloEGest = new RicercaPuntualeCapitoloEGest();
		ricercaPuntualeCapitoloEGest.setAnnoCapitolo(getCapitoloEntrataGestione().getAnnoCapitolo());
		ricercaPuntualeCapitoloEGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaPuntualeCapitoloEGest.setNumeroCapitolo(getCapitoloEntrataGestione().getNumeroCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroArticolo(getCapitoloEntrataGestione().getNumeroArticolo());
		ricercaPuntualeCapitoloEGest.setNumeroUEB(getCapitoloEntrataGestione().getNumeroUEB());
		ricercaPuntualeCapitoloEGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		request.setRicercaPuntualeCapitoloEGest(ricercaPuntualeCapitoloEGest);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaPuntualeCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaPuntualeCapitoloUscitaGestione creaRequestRicercaPuntualeCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUscitaGestione request = creaRequest(RicercaPuntualeCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		
		RicercaPuntualeCapitoloUGest ricercaPuntualeCapitoloUGest = new RicercaPuntualeCapitoloUGest();
		ricercaPuntualeCapitoloUGest.setAnnoCapitolo(getCapitoloUscitaGestione().getAnnoCapitolo());
		ricercaPuntualeCapitoloUGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaPuntualeCapitoloUGest.setNumeroCapitolo(getCapitoloUscitaGestione().getNumeroCapitolo());
		ricercaPuntualeCapitoloUGest.setNumeroArticolo(getCapitoloUscitaGestione().getNumeroArticolo());
		ricercaPuntualeCapitoloUGest.setNumeroUEB(getCapitoloUscitaGestione().getNumeroUEB());
		ricercaPuntualeCapitoloUGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		request.setRicercaPuntualeCapitoloUGest(ricercaPuntualeCapitoloUGest);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}
	
	//SIAC-5060
	/**
	 * Crea una request per il servizio {@link Liste}
	 * @param tipiLista i tipi di lista da cercare
	 * @return la request creata
	 */
	public Liste creaRequestListe(TipiLista... tipiLista) {
		Liste request = creaRequest(Liste.class);
		
		request.setEnte(getEnte());
		request.setTipi(tipiLista);
		request.setBilancio(getBilancio());
		
		return request;
	}

	/**
	 * Aggiunge una causale di entrata.
	 */
	public void addCausaleEntrata() {
		CausaleEntrata ce = new CausaleEntrata();
		
		Accertamento a = obtainAccertamentoData(getAccertamento());
		SubAccertamento sa = obtainSubAccertamentoData(getSubAccertamento());
		
		ce.setAccertamento(a);
		ce.setSubAccertamento(sa);
		ce.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitoloEntrataGestione()));
		ce.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		//SIAC-5060
		ce.setDistinta(getDistinta());

		// Impostazione della causale nella lista
		getListaCausaleEntrata().add(ce);
	}
	
	/**
	 * Aggiunge una causale di spesa.
	 */
	public void addCausaleSpesa() {
		CausaleSpesa cs = new CausaleSpesa();
		
		Impegno i = obtainImpegnoData(getImpegno());
		SubImpegno si = obtainSubImpegnoData(getSubImpegno());
		
		cs.setImpegno(i);
		cs.setSubImpegno(si);
		cs.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitoloUscitaGestione()));
		cs.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		
		// Impostazione della causale nella lista
		getListaCausaleSpesa().add(cs);
	}
	
	/**
	 * Ottiene i dati dall'accertamento.
	 * 
	 * @param acc l'accertamento
	 * 
	 * @return l'accertamento con i soli dati necessarii
	 */
	private Accertamento obtainAccertamentoData(Accertamento acc) {
		if(impostaEntitaFacoltativa(acc) == null) {
			return null;
		}
		Accertamento res = new Accertamento();
		populateMovimentoGestione(acc, res);
		res.setDisponibilitaIncassare(acc.getDisponibilitaIncassare());
		return res;
	}
	
	/**
	 * Ottiene i dati dal subaccertamento.
	 * 
	 * @param subAcc il subaccertamento
	 * 
	 * @return il subaccertamento con i soli dati necessarii
	 */
	private SubAccertamento obtainSubAccertamentoData(SubAccertamento subAcc) {
		if(impostaEntitaFacoltativa(subAcc) == null) {
			return null;
		}
		SubAccertamento res = new SubAccertamento();
		populateMovimentoGestione(subAcc, res);
		res.setDisponibilitaIncassare(subAcc.getDisponibilitaIncassare());
		return res;
	}
	
	/**
	 * Ottiene i dati dall'impegno.
	 * 
	 * @param imp l'impegno
	 * 
	 * @return l'impegno con i soli dati necessarii
	 */
	private Impegno obtainImpegnoData(Impegno imp) {
		if(impostaEntitaFacoltativa(imp) == null) {
			return null;
		}
		Impegno res = new Impegno();
		populateMovimentoGestione(imp, res);
		res.setDisponibilitaPagare(imp.getDisponibilitaPagare());
		return res;
	}
	
	/**
	 * Ottiene i dati dal subimpegno.
	 * 
	 * @param subImp il subimpegno
	 * 
	 * @return il subimpegno con i soli dati necessarii
	 */
	private SubImpegno obtainSubImpegnoData(SubImpegno subImp) {
		if(impostaEntitaFacoltativa(subImp) == null) {
			return null;
		}
		SubImpegno res = new SubImpegno();
		populateMovimentoGestione(subImp, res);
		res.setDisponibilitaPagare(subImp.getDisponibilitaPagare());
		return res;
	}
	
	/**
	 * Popola il movimento di gestione.
	 * 
	 * @param original l'originale
	 * @param copy     la copia
	 */
	private void populateMovimentoGestione(MovimentoGestione original, MovimentoGestione copy) {
		copy.setUid(original.getUid());
		copy.setAnnoCapitoloOrigine(original.getAnnoCapitoloOrigine());
		copy.setAnnoMovimento(original.getAnnoMovimento());
		copy.setNumeroBigDecimal(original.getNumeroBigDecimal());
		copy.setDescrizione(original.getDescrizione());
		copy.setImportoAttuale(original.getImportoAttuale());
	}
	
	
	/**
	 * Ottiene la descrizione completa del soggetto fornito in input.
	 * 
	 * @param sogg il soggetto la cui descrizione &eacute; da essere calcolata
	 * 
	 * @return la descrizione completa del soggetto
	 */
	protected String computeDescrizioneCompletaSoggetto(final Soggetto sogg) {
		final StringBuilder sb = new StringBuilder();
		final String separator = " - ";
		if((sogg != null) && (sogg.getUid() != 0)) {
			sb.append(sogg.getCodiceSoggetto())
				.append(separator)
				.append(sogg.getDenominazione())
				.append(separator)
				.append(sogg.getCodiceFiscale());
		}
		return sb.toString();
	}
	
	/**
	 * Ottiene l'indirizzo completo della sede secondaria fornito in input.
	 * 
	 * @param sede la sedeSecondariaSoggetto il cui indirizzo deve essere calcolato
	 * 
	 * @return indirizzo completo della sede secondaria
	 */
	protected String computeIndirizzoCompletoSedeSecondariaSoggetto(final SedeSecondariaSoggetto sede) {
		final StringBuilder sb = new StringBuilder();
		final String separator1 = " ";
		final String separator2 = " , ";
		if((sede != null) && (sede.getUid() != 0)) {
			sb.append(sede.getIndirizzoSoggettoPrincipale().getSedime())
				.append(separator1)
				.append(sede.getIndirizzoSoggettoPrincipale().getDenominazione())
				.append(separator2)
				.append(sede.getIndirizzoSoggettoPrincipale().getNumeroCivico());
		}
		return sb.toString();
	}
	
	/**
	 * Ottiene la descrizione completa della modalit&agrave; pagamento fornito in input.
	 * 
	 * @param modalitaPagamento la modalit&agrave; di pagamento la cui descrizione &eacute; da essere calcolata
	 * 
	 * @return la descrizione completa della modalit&agrave; pagamento
	 */
	protected String computeModalitaPagamentoCompletaSoggetto(final ModalitaPagamentoSoggetto modalitaPagamento) {
		final StringBuilder sb = new StringBuilder();
		final String separator = " - ";
		if((modalitaPagamento != null) && (modalitaPagamento.getUid() != 0)) {
			sb.append(modalitaPagamento.getModalitaAccreditoSoggetto().getCodice())
				.append(separator)
				.append(modalitaPagamento.getModalitaAccreditoSoggetto().getDescrizione());

		}
		return sb.toString();
	}
}
