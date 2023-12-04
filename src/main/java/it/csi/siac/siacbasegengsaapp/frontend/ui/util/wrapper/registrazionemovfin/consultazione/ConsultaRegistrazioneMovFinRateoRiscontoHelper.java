/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.RateoRisconto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.Risconto;

/**
 * Helper per la consultazione dei dati della generica entit&agrave;
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinRateoRiscontoHelper extends ConsultaRegistrazioneMovFinBaseHelper<RateoRisconto> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	
	private final RateoRisconto rateoRisconto;
	private final PrimaNota primaNota;
	private final RegistrazioneMovFin registrazioneMovFin;
	private final String tipoRateoRisconto;
	
	private String tipoMovimentoRateoRisconto;
	private String annoMovimentoRateoRisconto;
	private String numeroMovimentoRateoRisconto;
	private String numeroSubMovimentoRateoRisconto;
	private String descrizioneMovimentoRateoRisconto;
	private String soggettoMovimentoRateoRisconto;
	
	/**
	 * Costruttore di wrap
	 * @param rateoRisconto il rateo/risconto
	 * @param primaNota la prima nota
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinRateoRiscontoHelper(RateoRisconto rateoRisconto, PrimaNota primaNota, boolean isGestioneUEB) {
		super(isGestioneUEB);
		this.rateoRisconto = rateoRisconto;
		this.primaNota = primaNota;
		this.registrazioneMovFin = retrieveRegistrazioneMovFin();
		this.tipoRateoRisconto = retrieveTipoRateoRisconto();
		impostaMovimentoRateoRisconto();
	}

	/**
	 * @return the rateoRisconto
	 */
	public RateoRisconto getRateoRisconto() {
		return this.rateoRisconto;
	}

	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return this.primaNota;
	}

	/**
	 * @return the registrazioneMovFin
	 */
	public RegistrazioneMovFin getRegistrazioneMovFin() {
		return this.registrazioneMovFin;
	}

	@Override
	public String getDatiCreazioneModifica() {
		return calcolaDatiCreazioneModifica(rateoRisconto.getDataCreazione(),
				rateoRisconto.getLoginOperazione(),
				rateoRisconto.getDataModifica(),
				rateoRisconto.getLoginOperazione());
	}
	
	/**
	 * @return the tipoRateoRisconto
	 */
	public String getTipoRateoRisconto() {
		return tipoRateoRisconto;
	}
	/**
	 * @return the tipoMovimentoRateoRisconto
	 */
	public String getTipoMovimentoRateoRisconto() {
		return tipoMovimentoRateoRisconto;
	}
	/**
	 * @return the annoMovimentoRateoRisconto
	 */
	public String getAnnoMovimentoRateoRisconto() {
		return annoMovimentoRateoRisconto;
	}
	/**
	 * @return the numeroMovimentoRateoRisconto
	 */
	public String getNumeroMovimentoRateoRisconto() {
		return numeroMovimentoRateoRisconto;
	}
	/**
	 * @return the numeroSubMovimentoRateoRisconto
	 */
	public String getNumeroSubMovimentoRateoRisconto() {
		return this.numeroSubMovimentoRateoRisconto;
	}

	/**
	 * @return the descrizionemovimentoRateoRisconto
	 */
	public String getDescrizioneMovimentoRateoRisconto() {
		return descrizioneMovimentoRateoRisconto;
	}
	/**
	 * @return the soggettoMovimentoRateoRisconto
	 */
	public String getSoggettoMovimentoRateoRisconto () {
		return soggettoMovimentoRateoRisconto;
	}
	
	
	/**
	 * Recupera la registrazione movfin
	 * @return la registrazione
	 */
	private RegistrazioneMovFin retrieveRegistrazioneMovFin() {
		return primaNota.getListaMovimentiEP().get(0).getRegistrazioneMovFin();
	}
	
	/**
	 * Recupera il tipo rateo/risconto
	 * @return il tipo
	 */
	private String retrieveTipoRateoRisconto() {
		return rateoRisconto instanceof Rateo ? "Rateo" :  rateoRisconto instanceof Risconto ? "Risconto" : "";
	}
	
	/**
	 * Impostazione dei dati del rateo/risconto
	 */
	private void impostaMovimentoRateoRisconto() {
		Entita movimento = registrazioneMovFin.getMovimento();
		if(movimento instanceof MovimentoGestione) {
			MovimentoGestione movimentoGestione = (MovimentoGestione) movimento;
			impostaDatiConsultazioneMovimento("Impegno",
					Integer.toString(movimentoGestione.getAnnoMovimento()),
					FormatUtils.formatPlain(movimentoGestione.getNumeroBigDecimal()),
					movimentoGestione.getDescrizione(),
					movimentoGestione.getSoggetto(),
					null);
			return;
		}
		if(movimento instanceof Ordinativo) {
			Ordinativo ordinativo = (Ordinativo) movimento;
			impostaDatiConsultazioneMovimento("Ordinativo",
					ordinativo.getAnno().toString(),
					ordinativo.getNumero().toString(),
					ordinativo.getDescrizione(),
					ordinativo.getSoggetto(),
					null);
			return;
		}
		
		if(movimento instanceof Liquidazione) {
			Liquidazione liquidazione = (Liquidazione) movimento;
			impostaDatiConsultazioneMovimento("Liquidazione",
					liquidazione.getAnnoLiquidazione().toString(),
					FormatUtils.formatPlain(liquidazione.getNumeroLiquidazione()),
					liquidazione.getDescrizioneLiquidazione(),
					liquidazione.getSoggettoLiquidazione(),
					null);
			return;
		}
		
		if(movimento instanceof Subdocumento<?,?>) {
			Subdocumento<?,?> subdoc = (Subdocumento<?,?>) movimento;
			Documento<?,?> doc = subdoc.getDocumento();
			impostaDatiConsultazioneMovimento("Subdocumento",
					doc.getAnno().toString(),
					doc.getNumero().toString(),
					subdoc.getDescrizione(),
					doc.getSoggetto(),
					subdoc.getNumero().toString());
			return;
		}
		
	}
	
	/**
	 * Imposta dati consultazione movimento.
	 *
	 * @param tipoMovimento il tipo movimento: liquidazione, impegno, accertamento, subdocumento
	 * @param anno the anno del movimento associato al rateo
	 * @param numero the numero del movimento associato al rateo
	 * @param descrizione the descrizione del movimento associato al rateo
	 * @param soggetto the soggetto associato al movimento del rateo
	 * @param numeroSubMovimento the numero sub movimento
	 */
	public void impostaDatiConsultazioneMovimento(String tipoMovimento,String anno, String numero, String descrizione, Soggetto soggetto, String numeroSubMovimento) {
		tipoMovimentoRateoRisconto = tipoMovimento;
		annoMovimentoRateoRisconto = anno;
		numeroMovimentoRateoRisconto = numero;
		descrizioneMovimentoRateoRisconto = descrizione;
		numeroSubMovimentoRateoRisconto = numeroSubMovimento;
		if(soggetto != null) {
			soggettoMovimentoRateoRisconto = soggetto.getCodiceSoggetto() + " - " + soggetto.getDenominazione();
		}
	}
}
