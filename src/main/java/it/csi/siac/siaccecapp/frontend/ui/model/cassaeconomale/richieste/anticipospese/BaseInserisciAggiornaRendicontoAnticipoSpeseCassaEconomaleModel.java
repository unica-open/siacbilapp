/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.model.Valuta;


/**
 * Classe base di model per l'inserimento e l'aggiornamento del rendiconto per l'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleModel extends BaseInserisciAggiornaAnticipoSpeseCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7483459526219489546L;
	
	private RendicontoRichiesta rendicontoRichiesta;
	private RendicontoRichiesta rendicontoRichiestaCopia;
	private List<Giustificativo> listaGiustificativo = new ArrayList<Giustificativo>();
	
	private List<TipoGiustificativo> listaTipoGiustificativo = new ArrayList<TipoGiustificativo>();
	private List<Valuta> listaValuta = new ArrayList<Valuta>();
	
	private Integer uidValutaEuro;
	private Giustificativo giustificativo;
	private Integer rowNumber;
	
	private Boolean restituzioneTotale = Boolean.FALSE;
	private Integer uidFattura;
	
	/**
	 * @return the rendicontoRichiesta
	 */
	public RendicontoRichiesta getRendicontoRichiesta() {
		return rendicontoRichiesta;
	}

	/**
	 * @param rendicontoRichiesta the rendicontoRichiesta to set
	 */
	public void setRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		this.rendicontoRichiesta = rendicontoRichiesta;
	}
	
	/**
	 * @return the uidValutaEuro
	 */
	public Integer getUidValutaEuro() {
		return uidValutaEuro;
	}

	/**
	 * @param uidValutaEuro the uidValutaEuro to set
	 */
	public void setUidValutaEuro(Integer uidValutaEuro) {
		this.uidValutaEuro = uidValutaEuro;
	}

	/**
	 * @return the listaGiustificativo
	 */
	public List<Giustificativo> getListaGiustificativo() {
		return listaGiustificativo;
	}

	/**
	 * @param listaGiustificativo the listaGiustificativo to set
	 */
	public void setListaGiustificativo(List<Giustificativo> listaGiustificativo) {
		this.listaGiustificativo = listaGiustificativo;
	}
	
	/**
	 * @return the listaTipoGiustificativo
	 */
	public List<TipoGiustificativo> getListaTipoGiustificativo() {
		return listaTipoGiustificativo;
	}

	/**
	 * @param listaTipoGiustificativo the listaTipoGiustificativo to set
	 */
	public void setListaTipoGiustificativo(
			List<TipoGiustificativo> listaTipoGiustificativo) {
		this.listaTipoGiustificativo = listaTipoGiustificativo;
	}
	
	/**
	 * @return the listaValuta
	 */
	public List<Valuta> getListaValuta() {
		return listaValuta;
	}

	/**
	 * @param listaValuta the listaValuta to set
	 */
	public void setListaValuta(List<Valuta> listaValuta) {
		this.listaValuta = listaValuta;
	}

	/**
	 * @return the giustificativo
	 */
	public Giustificativo getGiustificativo() {
		return giustificativo;
	}

	/**
	 * @param giustificativo the giustificativo to set
	 */
	public void setGiustificativo(Giustificativo giustificativo) {
		this.giustificativo = giustificativo;
	}
	
	/**
	 * @return the rowNumber
	 */
	public Integer getRowNumber() {
		return rowNumber;
	}

	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * @return the rendicontoRichiestaCopia
	 */
	public RendicontoRichiesta getRendicontoRichiestaCopia() {
		return rendicontoRichiestaCopia;
	}

	/**
	 * @param rendicontoRichiestaCopia the rendicontoRichiestaCopia to set
	 */
	public void setRendicontoRichiestaCopia(RendicontoRichiesta rendicontoRichiestaCopia) {
		this.rendicontoRichiestaCopia = rendicontoRichiestaCopia;
	}

	/**
	 * @return the restituzioneTotale
	 */
	public Boolean getRestituzioneTotale() {
		return restituzioneTotale;
	}

	/**
	 * @param restituzioneTotale the restituzioneTotale to set
	 */
	public void setRestituzioneTotale(Boolean restituzioneTotale) {
		this.restituzioneTotale = restituzioneTotale != null ? restituzioneTotale : Boolean.FALSE;
	}

	/**
	 * @return the uidFattura
	 */
	public Integer getUidFattura() {
		return uidFattura;
	}

	/**
	 * @param uidFattura the uidFattura to set
	 */
	public void setUidFattura(Integer uidFattura) {
		this.uidFattura = uidFattura;
	}

	/**
	 * @return the datiEconomoCompilati
	 */
	public boolean isDatiEconomoCompilati() {
		return getRendicontoRichiesta() != null && getRendicontoRichiesta().getMovimento() != null && getRendicontoRichiesta().getMovimento().getDataMovimento() != null
				&& getRendicontoRichiesta().getMovimento().getModalitaPagamentoCassa() != null && getRendicontoRichiesta().getMovimento().getModalitaPagamentoCassa().getUid() != 0;
	}
	
	@Override
	protected RichiestaEconomale ottieniRichiestaEconomalePerRiepilogo() {
		return getRendicontoRichiesta().getRichiestaEconomale();
	}
	
	/**
	 * @return the importoRichiestaAnticipato
	 */
	public BigDecimal getImportoRichiestaAnticipato() {
		return getRendicontoRichiesta() != null && getRendicontoRichiesta().getRichiestaEconomale() != null && getRendicontoRichiesta().getRichiestaEconomale().getImporto() != null
				? getRendicontoRichiesta().getRichiestaEconomale().getImporto()
				: BigDecimal.ZERO;
	}
	
	/**
	 * @return the importoDaRestituire
	 */
	public BigDecimal getImportoDaRestituire() {
//		if((getListaGiustificativo() == null || getListaGiustificativo().isEmpty()) && !Boolean.TRUE.equals(getRestituzioneTotale())) {
//			return BigDecimal.ZERO;
//		}
		BigDecimal importo = Boolean.TRUE.equals(getRestituzioneTotale())
				? getImportoRichiestaAnticipato()
				: getImportoRichiestaAnticipato().subtract(getTotaleImportiGiustificativi());
		return importo.max(BigDecimal.ZERO);
	}
	
	/**
	 * @return the importoDaIntegrare
	 */
	public BigDecimal getImportoDaIntegrare() {
		return getTotaleImportiGiustificativi().subtract(getImportoRichiestaAnticipato()).max(BigDecimal.ZERO);
	}
	
	/**
	 * @return the capitoloMovimentoRichiesta
	 */
	public CapitoloUscitaGestione getCapitoloMovimentoRichiesta() {
		return getRendicontoRichiesta() != null && getRendicontoRichiesta().getRichiestaEconomale() != null && getRendicontoRichiesta().getRichiestaEconomale().getImpegno() != null
				? getRendicontoRichiesta().getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione()
				: null;
	}
	
	/**
	 * @return the descrizioneCapitoloMovimentoRichiesta
	 */
	public String getDescrizioneCapitoloMovimentoRichiesta() {
		CapitoloUscitaGestione cug = getCapitoloMovimentoRichiesta();
		return computeDescrizioneCapitolo(cug);
	}
	
	@Override
	public boolean isCopied() {
		return getRendicontoRichiestaCopia() != null && getRendicontoRichiestaCopia().getUid() != 0;
	}
	
	/**
	 * @return the totaleImportiGiustificativi
	 */
	public BigDecimal getTotaleImportiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo g : getListaGiustificativo()) {
			if(g.getImportoGiustificativo() != null) {
				totale = totale.add(g.getImportoGiustificativo());
			}
		}
		return totale;
	}
	
	@Override
	public String getDenominazioneRichiestaPerRendiconto() {
		return "Dati della richiesta di anticipo spese";
	}
	
	@Override
	public String getStringaRiepilogoRichiestaEconomale() {
		StringBuilder sb = new StringBuilder();
		
		RichiestaEconomale re = ottieniRichiestaEconomalePerRiepilogo();
		
		if(re != null && re.getUid() != 0) {
			// Richiesta numero
			sb.append(computaStringaNumeroRichiesta(re))
				// Richiesta del
				.append(computaStringaDataRichiesta(re))
				// Stato richiesta
				.append(computaStringaStatoRichiesta(re))
				// Sospeso
				.append(computaStringaSospeso(re))
				// Movimento
				.append(computaStringaMovimento(getRendicontoRichiesta()));
		}
		
		return sb.toString();
	}
	
	@Override
	protected String computaStringaSospeso(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale == null || richiestaEconomale.getSospeso() == null) {
			return "";
		}
		return " - Sospeso N. " + richiestaEconomale.getSospeso().getNumeroSospeso();
	}
	
	
	/**
	 * Ottiene il movimento da considerare.
	 * 
	 * @return il movimento
	 */
	@Override
	protected Movimento ottieniMovimento() {
		// To override in subclasses for Rendiconto
		return getRendicontoRichiesta().getMovimento();
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo() {
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.RIMBORSO);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaValuta}.
	 * 
	 * @return la request creata
	 */
	public RicercaValuta creaRequestRicercaValuta() {
		RicercaValuta request = creaRequest(RicercaValuta.class);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceRichiestaEconomale}.
	 * request per stampa ricevuta
	 * @return la request creata
	 */
	public StampaRicevutaRendicontoRichiestaEconomale creaRequestStampaRicevutaRendicontoRichiestaEconomale() {
		StampaRicevutaRendicontoRichiestaEconomale request = creaRequest(StampaRicevutaRendicontoRichiestaEconomale.class);
		request.setBilancio(getBilancio());
		request.setRichiedente(getRichiedente());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRendicontoRichiesta(getRendicontoRichiesta() != null ? getRendicontoRichiesta() : getRendicontoRichiestaCopia());
		
		return request;
	}
	
	@Override
	public String getDatiMatricola() {
		if(getRendicontoRichiesta() == null || getRendicontoRichiesta().getRichiestaEconomale() == null) {
			return "";
		}
		if(getRendicontoRichiesta().getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getSoggetto().getMatricola());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getMatricola());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getNome());
		if (!isGestioneHR()) {
			//hr mette tutti i dati in un solo campo
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getCognome());
		}
		
		return StringUtils.join(chunks, " - ");
	}
}
