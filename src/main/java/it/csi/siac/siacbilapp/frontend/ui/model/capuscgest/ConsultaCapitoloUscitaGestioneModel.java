/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.DisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileFilter;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;

/**
 * Classe di model per la consultazione del Capitolo di Uscita Gestione.
 * Contiene una mappatura dei form per il Capitolo di Uscita Gestione.
 *
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
public class ConsultaCapitoloUscitaGestioneModel extends CapitoloUscitaModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 819970919083159633L;
	private static final BigDecimal TWELVE = new BigDecimal(12);

	/* Maschera 1 */
	private CapitoloUscitaGestione capitolo;

	private ImportiCapitoloUG importiEsercizioPrecedente;
	private ImportiCapitoloUG importiCapitoloUscitaGestione0;
	private ImportiCapitoloUG importiCapitoloUscitaGestione1;
	private ImportiCapitoloUG importiCapitoloUscitaGestione2;
	
	
	//6881 NUOVI DATI
	private ImportiCapitoloPerComponente competenzaStanziamento;
	private ImportiCapitoloPerComponente competenzaImpegnato;
	private ImportiCapitoloPerComponente disponibilita;
	private ImportiCapitoloPerComponente residuiEffettivi;
	private ImportiCapitoloPerComponente residuiPresunti;
	private ImportiCapitoloPerComponente cassaStanziato;
	private ImportiCapitoloPerComponente cassaPagato;
	
	
	

	// Per il dettaglio
	private Boolean variazioneInAumento;

	// CR-4324
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno0;
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno1;
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno2;
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneResiduo;

	// SIAC-4585
	private boolean hasMassimoImpegnabile;

	// SIAC-6193
	private List<EntitaConsultabileFilter> listaFiltroImpegno = new ArrayList<EntitaConsultabileFilter>();
	private List<EntitaConsultabileFilter> listaFiltroLiquidazione = new ArrayList<EntitaConsultabileFilter>();
	private List<EntitaConsultabileFilter> listaFiltroOrdinativo = new ArrayList<EntitaConsultabileFilter>();
	// SIAC-6305
	private String openTab;

	// GESC0012
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	
	//per importi residui e anni successivi
	private ImportiCapitolo importiCapitoloSuccessivi;
	private ImportiCapitolo importiResidui;
	
	//SIAC-7349 Start - SR210 -MR - 16/04/2020 Setting delle disponibilita dei componenti nel model
	private List<ImportiCapitoloPerComponente> disponibilitaImpegnareComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	//SIAC-7349 - End
	
	//SIAC-7349 Start - SR210 -MR - 16/04/2020 Setting delle disponibilita a variare dei componenti nel model
	private List<ImportiCapitoloPerComponente> disponibilitaVariareComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	//SIAC-7349 - End
	/** Costruttore vuoto di default */
	public ConsultaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Consultazione Capitolo Spesa Gestione");
	}

	/* Getter e setter */

	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo
	 *            the capitoloUscitaPrevisione to set
	 */
	public void setCapitolo(CapitoloUscitaGestione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the importiCapitoloUscitaGestione0
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione0() {
		return importiCapitoloUscitaGestione0;
	}

	/**
	 * @param importiCapitoloUscitaGestione0
	 *            the importiCapitoloUscitaGestione0 to set
	 */
	public void setImportiCapitoloUscitaPrevisione0(ImportiCapitoloUG importiCapitoloUscitaGestione0) {
		this.importiCapitoloUscitaGestione0 = importiCapitoloUscitaGestione0;
	}

	/**
	 * @return the importiCapitoloUscitaGestione1
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione1() {
		return importiCapitoloUscitaGestione1;
	}

	/**
	 * @param importiCapitoloUscitaGestione1
	 *            the importiCapitoloUscitaGestione1 to set
	 */
	public void setImportiCapitoloUscitaGestione1(ImportiCapitoloUG importiCapitoloUscitaGestione1) {
		this.importiCapitoloUscitaGestione1 = importiCapitoloUscitaGestione1;
	}

	/**
	 * @return the importiCapitoloUscitaGestione2
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione2() {
		return importiCapitoloUscitaGestione2;
	}

	/**
	 * @param importiCapitoloUscitaGestione2
	 *            the importiCapitoloUscitaGestione2 to set
	 */
	public void setImportiCapitoloUscitaGestione2(ImportiCapitoloUG importiCapitoloUscitaGestione2) {
		this.importiCapitoloUscitaGestione2 = importiCapitoloUscitaGestione2;
	}

	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		this.importiComponentiCapitolo = importiComponentiCapitolo;
	}

	public List<ImportiCapitoloPerComponente> getDisponibilitaImpegnareComponentiCapitolo() {
		return disponibilitaImpegnareComponentiCapitolo;
	}

	public void setDisponibilitaImpegnareComponentiCapitolo(
			List<ImportiCapitoloPerComponente> disponibilitaImpegnareComponentiCapitolo) {
		this.disponibilitaImpegnareComponentiCapitolo = disponibilitaImpegnareComponentiCapitolo;
	}

	public List<ImportiCapitoloPerComponente> getDisponibilitaVariareComponentiCapitolo() {
		return disponibilitaVariareComponentiCapitolo;
	}

	public void setDisponibilitaVariareComponentiCapitolo(
			List<ImportiCapitoloPerComponente> disponibilitaVariareComponentiCapitolo) {
		this.disponibilitaVariareComponentiCapitolo = disponibilitaVariareComponentiCapitolo;
	}

	public ImportiCapitolo getImportiCapitoloSuccessivi() {
		return importiCapitoloSuccessivi;
	}

	public void setImportiCapitoloSuccessivi(ImportiCapitolo importiCapitoloSuccessivi) {
		this.importiCapitoloSuccessivi = importiCapitoloSuccessivi;
	}

	public ImportiCapitolo getImportiResidui() {
		return importiResidui;
	}

	public void setImportiResidui(ImportiCapitolo importiResidui) {
		this.importiResidui = importiResidui;
	}

	/**
	 * Ottiene l'uid del capitolo da consultare.
	 *
	 * @return l'uid del Capitolo di Uscita Previsione da consultare
	 */
	public int getUidDaConsultare() {
		return capitolo == null ? 0 : capitolo.getUid();
	}

	/**
	 * Imposta l'uid nel Capitolo di Uscita Previsione da consultare.
	 *
	 * @param uidDaConsultare
	 *            l'uid da impostare
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		if (capitolo == null) {
			capitolo = new CapitoloUscitaGestione();
		}
		capitolo.setUid(uidDaConsultare);
	}

	/**
	 * @return the variazioneInAumento
	 */
	public Boolean getVariazioneInAumento() {
		return variazioneInAumento;
	}

	/**
	 * @param variazioneInAumento
	 *            the variazioneInAumento to set
	 */
	public void setVariazioneInAumento(Boolean variazioneInAumento) {
		this.variazioneInAumento = variazioneInAumento;
	}

	/**
	 * @param importiCapitoloUscitaGestione0
	 *            the importiCapitoloUscitaGestione0 to set
	 */
	public void setImportiCapitoloUscitaGestione0(ImportiCapitoloUG importiCapitoloUscitaGestione0) {
		this.importiCapitoloUscitaGestione0 = importiCapitoloUscitaGestione0;
	}

	/**
	 * @return the importiEsercizioPrecedente
	 */
	public ImportiCapitoloUG getImportiEsercizioPrecedente() {
		return importiEsercizioPrecedente;
	}

	/**
	 * @param importiEsercizioPrecedente
	 *            the importiEsercizioPrecedente to set
	 */
	public void setImportiEsercizioPrecedente(ImportiCapitoloUG importiEsercizioPrecedente) {
		this.importiEsercizioPrecedente = importiEsercizioPrecedente;
	}

	/**
	 * @return the disponibilitaCapitoloUscitaGestioneAnno0
	 */
	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneAnno0() {
		return disponibilitaCapitoloUscitaGestioneAnno0;
	}

	/**
	 * @param disponibilitaCapitoloUscitaGestioneAnno0
	 *            the disponibilitaCapitoloUscitaGestioneAnno0 to set
	 */
	public void setDisponibilitaCapitoloUscitaGestioneAnno0(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno0) {
		this.disponibilitaCapitoloUscitaGestioneAnno0 = disponibilitaCapitoloUscitaGestioneAnno0;
	}

	/**
	 * @return the disponibilitaCapitoloUscitaGestioneAnno1
	 */
	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneAnno1() {
		return disponibilitaCapitoloUscitaGestioneAnno1;
	}

	/**
	 * @param disponibilitaCapitoloUscitaGestioneAnno1
	 *            the disponibilitaCapitoloUscitaGestioneAnno1 to set
	 */
	public void setDisponibilitaCapitoloUscitaGestioneAnno1(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno1) {
		this.disponibilitaCapitoloUscitaGestioneAnno1 = disponibilitaCapitoloUscitaGestioneAnno1;
	}

	/**
	 * @return the disponibilitaCapitoloUscitaGestioneAnno2
	 */
	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneAnno2() {
		return disponibilitaCapitoloUscitaGestioneAnno2;
	}

	/**
	 * @param disponibilitaCapitoloUscitaGestioneAnno2
	 *            the disponibilitaCapitoloUscitaGestioneAnno2 to set
	 */
	public void setDisponibilitaCapitoloUscitaGestioneAnno2(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno2) {
		this.disponibilitaCapitoloUscitaGestioneAnno2 = disponibilitaCapitoloUscitaGestioneAnno2;
	}

	/**
	 * @return the disponibilitaCapitoloUscitaGestioneResiduo
	 */
	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneResiduo() {
		return disponibilitaCapitoloUscitaGestioneResiduo;
	}

	/**
	 * @param disponibilitaCapitoloUscitaGestioneResiduo
	 *            the disponibilitaCapitoloUscitaGestioneResiduo to set
	 */
	public void setDisponibilitaCapitoloUscitaGestioneResiduo(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneResiduo) {
		this.disponibilitaCapitoloUscitaGestioneResiduo = disponibilitaCapitoloUscitaGestioneResiduo;
	}

	/**
	 * @return the hasMassimoImpegnabile
	 */
	public boolean isHasMassimoImpegnabile() {
		return hasMassimoImpegnabile;
	}

	/**
	 * @param hasMassimoImpegnabile
	 *            the hasMassimoImpegnabile to set
	 */
	public void setHasMassimoImpegnabile(boolean hasMassimoImpegnabile) {
		this.hasMassimoImpegnabile = hasMassimoImpegnabile;
	}

	/**
	 * @return the openTab
	 */
	public String getOpenTab() {
		return this.openTab;
	}

	/**
	 * @param openTab
	 *            the openTab to set
	 */
	public void setOpenTab(String openTab) {
		this.openTab = openTab;
	}
	
	
	////per tabella
	public ImportiCapitoloPerComponente getCompetenzaStanziamento() {
		return competenzaStanziamento;
	}

	public void setCompetenzaStanziamento(ImportiCapitoloPerComponente competenzaStanziamento) {
		this.competenzaStanziamento = competenzaStanziamento;
	}

	public ImportiCapitoloPerComponente getCompetenzaImpegnato() {
		return competenzaImpegnato;
	}

	public void setCompetenzaImpegnato(ImportiCapitoloPerComponente competenzaImpegnato) {
		this.competenzaImpegnato = competenzaImpegnato;
	}

	public ImportiCapitoloPerComponente getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(ImportiCapitoloPerComponente disponibilita) {
		this.disponibilita = disponibilita;
	}

	public ImportiCapitoloPerComponente getResiduiEffettivi() {
		return residuiEffettivi;
	}

	public void setResiduiEffettivi(ImportiCapitoloPerComponente residuiEffettivi) {
		this.residuiEffettivi = residuiEffettivi;
	}

	public ImportiCapitoloPerComponente getResiduiPresunti() {
		return residuiPresunti;
	}

	public void setResiduiPresunti(ImportiCapitoloPerComponente residuiPresunti) {
		this.residuiPresunti = residuiPresunti;
	}

	public ImportiCapitoloPerComponente getCassaStanziato() {
		return cassaStanziato;
	}

	public void setCassaStanziato(ImportiCapitoloPerComponente cassaStanziato) {
		this.cassaStanziato = cassaStanziato;
	}

	public ImportiCapitoloPerComponente getCassaPagato() {
		return cassaPagato;
	}

	public void setCassaPagato(ImportiCapitoloPerComponente cassaPagato) {
		this.cassaPagato = cassaPagato;
	}
	////////////////////////

	// SIAC-4585
	/**
	 * @return the massimoImpegnabile0
	 */
	public String getMassimoImpegnabile0() {
		return hasMassimoImpegnabile && getImportiCapitoloUscitaGestione0() != null
				&& getImportiCapitoloUscitaGestione0().getMassimoImpegnabile() != null
						? FormatUtils.formatCurrency(getImportiCapitoloUscitaGestione0().getMassimoImpegnabile()) : "-";
	}

	/**
	 * @return the massimoImpegnabile1
	 */
	public String getMassimoImpegnabile1() {
		return hasMassimoImpegnabile && getImportiCapitoloUscitaGestione1() != null
				&& getImportiCapitoloUscitaGestione1().getMassimoImpegnabile() != null
						? FormatUtils.formatCurrency(getImportiCapitoloUscitaGestione1().getMassimoImpegnabile()) : "-";
	}

	/**
	 * @return the massimoImpegnabile2
	 */
	public String getMassimoImpegnabile2() {
		return hasMassimoImpegnabile && getImportiCapitoloUscitaGestione2() != null
				&& getImportiCapitoloUscitaGestione2().getMassimoImpegnabile() != null
						? FormatUtils.formatCurrency(getImportiCapitoloUscitaGestione2().getMassimoImpegnabile()) : "-";
	}

	/**
	 * @return the listaFiltroImpegno
	 */
	public List<EntitaConsultabileFilter> getListaFiltroImpegno() {
		return this.listaFiltroImpegno;
	}

	/**
	 * @param listaFiltroImpegno
	 *            the listaFiltroImpegno to set
	 */
	public void setListaFiltroImpegno(List<EntitaConsultabileFilter> listaFiltroImpegno) {
		this.listaFiltroImpegno = listaFiltroImpegno;
	}

	/**
	 * @return the listaFiltroLiquidazione
	 */
	public List<EntitaConsultabileFilter> getListaFiltroLiquidazione() {
		return this.listaFiltroLiquidazione;
	}

	/**
	 * @param listaFiltroLiquidazione
	 *            the listaFiltroLiquidazione to set
	 */
	public void setListaFiltroLiquidazione(List<EntitaConsultabileFilter> listaFiltroLiquidazione) {
		this.listaFiltroLiquidazione = listaFiltroLiquidazione;
	}

	/**
	 * @return the listaFiltroOrdinativo
	 */
	public List<EntitaConsultabileFilter> getListaFiltroOrdinativo() {
		return this.listaFiltroOrdinativo;
	}

	/**
	 * @param listaFiltroOrdinativo
	 *            the listaFiltroOrdinativo to set
	 */
	public void setListaFiltroOrdinativo(List<EntitaConsultabileFilter> listaFiltroOrdinativo) {
		this.listaFiltroOrdinativo = listaFiltroOrdinativo;
	}

	/**
	 * Ritorna se siamo o no in fase provvisoria di bilancio
	 * 
	 * @return the faseProvvisoria
	 */
	public boolean isFaseProvvisoria() {
		return FaseBilancio.ESERCIZIO_PROVVISORIO.name()
				.equals(getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio().name());
	}

	/**
	 * Ritorna se le prenotazioni siano abilitate
	 * 
	 * @return the prenotazioneAbilitato
	 */
	public boolean isPrenotazioneAbilitato() {
		return isGestioneLivello(TipologiaGestioneLivelli.GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI,
				BilConstants.GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI);
	}

	/**
	 * 12° Regime Provvisorio calcolo per anno 0
	 * 
	 * @return 12° Regime Provvisorio
	 */
	public BigDecimal getRegimeProvvisorio0() {
		//SIAC-8697
		return calcolaRegimeProvvisorio(getImportiCapitoloUscitaGestione0().getStanziamento(),
				getDisponibilitaCapitoloUscitaGestioneAnno0().getImpegnatoDaRiaccertamento(),
				getDisponibilitaCapitoloUscitaGestioneAnno0().getImpegnatoDaEserciziPrec());
	}

	/**
	 * 12° Regime Provvisorio calcolo per anno 1
	 * 
	 * @return 12° Regime Provvisorio
	 */
	public BigDecimal getRegimeProvvisorio1() {
		//SIAC-8697
		return calcolaRegimeProvvisorio(getImportiCapitoloUscitaGestione1().getStanziamento(),
				getDisponibilitaCapitoloUscitaGestioneAnno1().getImpegnatoDaRiaccertamento(),
				getDisponibilitaCapitoloUscitaGestioneAnno1().getImpegnatoDaEserciziPrec());
	}

	/**
	 * 12° Regime Provvisorio calcolo per anno 2
	 * 
	 * @return 12° Regime Provvisorio
	 */
	public BigDecimal getRegimeProvvisorio2() {
		//SIAC-8697
		return calcolaRegimeProvvisorio(getImportiCapitoloUscitaGestione2().getStanziamento(),
				getDisponibilitaCapitoloUscitaGestioneAnno2().getImpegnatoDaRiaccertamento(),
				getDisponibilitaCapitoloUscitaGestioneAnno2().getImpegnatoDaEserciziPrec());
	}

	/**
	 * - al netto impegni reimp. e da anni preced. anno 0
	 * 
	 * @return - al netto impegni reimp. e da anni preced.
	 */
	public BigDecimal getNettoReimpEAnniPrecedenti0() {
		return getImportiCapitoloUscitaGestione0().getStanziamento()
				.subtract(getDisponibilitaCapitoloUscitaGestioneAnno0().getImpegnatoDaRiaccertamento())
				.subtract(getDisponibilitaCapitoloUscitaGestioneAnno0().getImpegnatoDaEserciziPrec());
	}

	/**
	 * - al netto impegni reimp. e da anni preced. anno 1
	 * 
	 * @return - al netto impegni reimp. e da anni preced.
	 */
	public BigDecimal getNettoReimpEAnniPrecedenti1() {
		return getImportiCapitoloUscitaGestione1().getStanziamento()
				.subtract(getDisponibilitaCapitoloUscitaGestioneAnno1().getImpegnatoDaRiaccertamento())
				.subtract(getDisponibilitaCapitoloUscitaGestioneAnno2().getImpegnatoDaEserciziPrec());
	}

	/**
	 * - al netto impegni reimp. e da anni preced. anno 2
	 * 
	 * @return - al netto impegni reimp. e da anni preced.
	 */
	public BigDecimal getNettoReimpEAnniPrecedenti2() {
		return getImportiCapitoloUscitaGestione2().getStanziamento()
				.subtract(getDisponibilitaCapitoloUscitaGestioneAnno2().getImpegnatoDaRiaccertamento())
				.subtract(getDisponibilitaCapitoloUscitaGestioneAnno2().getImpegnatoDaEserciziPrec());
	}

	/* Request */

	/**
	 * Crea una Request per il servizio di
	 * {@link RicercaDettaglioCapitoloUscitaGestione}.
	 *
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione() {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(creaUtilitaRicercaDettaglioCapitoloUscitaGestione(capitolo.getUid()));
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));

		return request;
	}

	/**
	 * Crea una Request per il servizio di
	 * {@link RicercaDisponibilitaCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDisponibilitaCapitoloUscitaGestione creaRequestRicercaDisponibilitaCapitoloUscitaGestione() {
		RicercaDisponibilitaCapitoloUscitaGestione req = creaRequest(RicercaDisponibilitaCapitoloUscitaGestione.class);

		req.setAnnoBilancio(getAnnoEsercizioInt());

		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(getCapitolo().getUid());
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);

		return req;
	}

	/**
	 * Crea una Request per il servizio di {@link RicercaTipoClassificatore}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoClassificatore creaRequestRicercaTipoClassificatore() {
		RicercaTipoClassificatore req = creaRequest(RicercaTipoClassificatore.class);

		req.setAnno(getAnnoEsercizioInt());
		req.setTipologieClassificatore(Arrays.asList(TipologiaClassificatore.CLASSIFICATORE_1,
				TipologiaClassificatore.CLASSIFICATORE_2, TipologiaClassificatore.CLASSIFICATORE_3,
				TipologiaClassificatore.CLASSIFICATORE_4, TipologiaClassificatore.CLASSIFICATORE_5,
				TipologiaClassificatore.CLASSIFICATORE_6, TipologiaClassificatore.CLASSIFICATORE_7,
				TipologiaClassificatore.CLASSIFICATORE_8, TipologiaClassificatore.CLASSIFICATORE_9,
				TipologiaClassificatore.CLASSIFICATORE_10, TipologiaClassificatore.CLASSIFICATORE_31,
				TipologiaClassificatore.CLASSIFICATORE_32, TipologiaClassificatore.CLASSIFICATORE_33,
				TipologiaClassificatore.CLASSIFICATORE_34, TipologiaClassificatore.CLASSIFICATORE_35));

		return req;
	}

	/* Metodi di utilita' */

	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita
	 * Gestione.
	 * 
	 * @param chiaveCapitolo
	 *            la chiave univoca del capitolo
	 *
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest creaUtilitaRicercaDettaglioCapitoloUscitaGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di
	 * {@link RicercaDettaglioCapitoloUscitaGestione} e dalla sessione.
	 *
	 * @param response
	 *            la Response del servizio di ricerca dettaglio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloUscitaGestioneResponse response) {
		/* Ottenuti dalla response */
		capitolo = response.getCapitoloUscita();

		/* Ottenuti dalla sessione */
		impostaClassificatoriGenericiDaLista(response.getCapitoloUscita().getClassificatoriGenerici());

		impostaImportiCapitoloUscitaGestione(capitolo.getListaImportiCapitoloUG());

		setBilancio(capitolo.getBilancio());
		setTipoFinanziamento(capitolo.getTipoFinanziamento());
		setTipoFondo(capitolo.getTipoFondo());
		setClassificazioneCofog(capitolo.getClassificazioneCofog());
		setElementoPianoDeiConti(capitolo.getElementoPianoDeiConti());
		setMacroaggregato(capitolo.getMacroaggregato());
		setMissione(capitolo.getMissione());
		setProgramma(capitolo.getProgramma());
		setStrutturaAmministrativoContabile(capitolo.getStrutturaAmministrativoContabile());
		setTitoloSpesa(capitolo.getTitoloSpesa());

		setSiopeSpesa(capitolo.getSiopeSpesa());
		setRicorrenteSpesa(capitolo.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitolo.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitolo.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitolo.getPoliticheRegionaliUnitarie());

		controllaImportiCapitoloEquivalente(capitolo, ImportiCapitoloUG.class);

		importiEsercizioPrecedente = capitolo.getImportiCapitoloEquivalente();
	}

	/**
	 * Imposta gli importi del Capitolo di Uscita Gestione a partire dalla
	 * Response del servizio di {@link RicercaDettaglioCapitoloUscitaGestione}.
	 *
	 * @param listaImporti
	 *            la lista degli importi
	 */
	private void impostaImportiCapitoloUscitaGestione(List<ImportiCapitoloUG> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiCapitoloUscitaGestione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloUscitaGestione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloUscitaGestione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
			importiEsercizioPrecedente = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt - 1));
		} catch (Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}

	/**
	 * Calcolo del regime provvisorio
	 * 
	 * @param stanziamentoIniziale
	 *            lo stanziamento iniziale
	 * @param impegnatoDaRiaccertamento
	 *            da riaccertamento
	 * @param impegnatoDaEserciziPrecedenti
	 *            da esercizi precedenti
	 * @return il regime provvisorio
	 */
	private BigDecimal calcolaRegimeProvvisorio(BigDecimal stanziamentoIniziale, BigDecimal impegnatoDaRiaccertamento,
			BigDecimal impegnatoDaEserciziPrecedenti) {
		return stanziamentoIniziale.subtract(impegnatoDaRiaccertamento).subtract(impegnatoDaEserciziPrecedenti)
				.divide(TWELVE, MathContext.DECIMAL128)
				.multiply(new BigDecimal(Calendar.getInstance().get(Calendar.MONTH) + 1))
				.setScale(2, RoundingMode.HALF_UP);
	}

	// GESC014
	/**
	 * Crea la request per la Ricerca Dettaglio dei componenti del Capitolo di Uscita Gestione.
	 * @param chiaveCapitolo la chiave univoca del capitolo
	 * 
	 * @return la request
	 */
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(capitolo.getUid());
		return request;
	}

}
